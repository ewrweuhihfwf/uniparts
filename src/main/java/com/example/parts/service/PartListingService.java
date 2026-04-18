package com.example.parts.service;

import com.example.parts.domain.AppUser;
import com.example.parts.domain.ListingImage;
import com.example.parts.domain.PartListing;
import com.example.parts.domain.PartCondition;
import com.example.parts.repository.PartListingRepository;
import com.example.parts.service.dto.ListingFilter;
import com.example.parts.service.dto.ListingForm;
import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PartListingService {

    private static final Logger log = LoggerFactory.getLogger(PartListingService.class);

    private final PartListingRepository partListingRepository;
    private final FileStorageService fileStorageService;

    public PartListingService(PartListingRepository partListingRepository, FileStorageService fileStorageService) {
        this.partListingRepository = partListingRepository;
        this.fileStorageService = fileStorageService;
    }

    @Transactional
    public PartListing createListing(ListingForm form, AppUser seller) {
        PartListing listing = new PartListing();
        applyFormValues(listing, form);
        listing.setSeller(seller);

        replaceImages(listing, fileStorageService.storeImages(form.getImages()));

        PartListing savedListing = partListingRepository.save(listing);
        log.info("Created listing id={} by seller={}", savedListing.getId(), seller.getEmail());
        return savedListing;
    }

    @Transactional(readOnly = true)
    public List<PartListing> getRecentListings(int limit) {
        return partListingRepository.findByOrderByCreatedAtDesc()
                .stream()
                .limit(limit)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PartListing> search(ListingFilter filter) {
        if (filter == null || filter.isEmpty()) {
            return partListingRepository.findByOrderByCreatedAtDesc();
        }

        Specification<PartListing> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            addContains(predicates, criteriaBuilder, root.get("title"), filter.getKeyword());
            addContains(predicates, criteriaBuilder, root.get("brand"), filter.getBrand());
            addContains(predicates, criteriaBuilder, root.get("model"), filter.getModel());
            addContains(predicates, criteriaBuilder, root.get("partCategory"), filter.getPartCategory());

            if (filter.getKeyword() != null && !filter.getKeyword().isBlank()) {
                String likePattern = "%" + filter.getKeyword().trim().toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), likePattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("brand")), likePattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("model")), likePattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("partCategory")), likePattern)
                ));
            }

            if (filter.getMinYear() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("vehicleYear"), filter.getMinYear()));
            }
            if (filter.getMaxYear() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("vehicleYear"), filter.getMaxYear()));
            }
            if (filter.getCondition() != null) {
                predicates.add(criteriaBuilder.equal(root.get("condition"), filter.getCondition()));
            }
            addMinPrice(predicates, criteriaBuilder, root.get("price"), filter.getMinPrice());
            addMaxPrice(predicates, criteriaBuilder, root.get("price"), filter.getMaxPrice());

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };

        return partListingRepository.findAll(specification, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @Transactional(readOnly = true)
    public PartListing getById(Long id) {
        return partListingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("განცხადება ვერ მოიძებნა"));
    }

    @Transactional(readOnly = true)
    public PartListing getOwnedListing(Long id, AppUser seller) {
        PartListing listing = getById(id);
        if (!listing.getSeller().getId().equals(seller.getId())) {
            throw new IllegalStateException("მხოლოდ განცხადების ავტორს შეუძლია რედაქტირება");
        }
        return listing;
    }

    @Transactional
    public PartListing updateListing(Long id, ListingForm form, AppUser seller) {
        PartListing listing = getOwnedListing(id, seller);
        applyFormValues(listing, form);

        List<String> newImages = fileStorageService.storeImages(form.getImages());
        List<String> oldImageFilenames = List.of();
        if (!newImages.isEmpty()) {
            oldImageFilenames = listing.getAllImageFilenames();
            replaceImages(listing, newImages);
        }

        PartListing savedListing = partListingRepository.save(listing);
        if (!oldImageFilenames.isEmpty()) {
            fileStorageService.deleteImages(oldImageFilenames);
        }
        log.info("Updated listing id={} by seller={}", savedListing.getId(), seller.getEmail());
        return savedListing;
    }

    @Transactional
    public void deleteListing(Long id, AppUser seller) {
        PartListing listing = getOwnedListing(id, seller);

        List<String> imageFilenames = listing.getAllImageFilenames();
        partListingRepository.delete(listing);
        fileStorageService.deleteImages(imageFilenames);
        log.info("Deleted listing id={} by seller={}", listing.getId(), seller.getEmail());
    }

    @Transactional(readOnly = true)
    public List<PartListing> getBySeller(AppUser seller) {
        return partListingRepository.findBySellerOrderByCreatedAtDesc(seller);
    }

    @Transactional(readOnly = true)
    public List<String> getAvailableBrands() {
        return partListingRepository.findByOrderByCreatedAtDesc().stream()
                .map(PartListing::getBrand)
                .distinct()
                .sorted()
                .toList();
    }

    @Transactional(readOnly = true)
    public List<String> getAvailableModels() {
        return partListingRepository.findByOrderByCreatedAtDesc().stream()
                .map(PartListing::getModel)
                .distinct()
                .sorted()
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Integer> getAvailableYears() {
        return partListingRepository.findByOrderByCreatedAtDesc().stream()
                .map(PartListing::getVehicleYear)
                .filter(year -> year != null)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .toList();
    }

    @Transactional(readOnly = true)
    public List<String> getAvailableCategories() {
        return partListingRepository.findByOrderByCreatedAtDesc().stream()
                .map(PartListing::getPartCategory)
                .distinct()
                .sorted()
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PartCondition> getAvailableConditions() {
        return List.of(PartCondition.values());
    }

    private void addContains(List<Predicate> predicates,
                             jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder,
                             jakarta.persistence.criteria.Path<String> path,
                             String value) {
        if (value != null && !value.isBlank()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(path), "%" + value.trim().toLowerCase() + "%"));
        }
    }

    private void addMinPrice(List<Predicate> predicates,
                             jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder,
                             jakarta.persistence.criteria.Path<BigDecimal> path,
                             BigDecimal value) {
        if (value != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(path, value));
        }
    }

    private void addMaxPrice(List<Predicate> predicates,
                             jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder,
                             jakarta.persistence.criteria.Path<BigDecimal> path,
                             BigDecimal value) {
        if (value != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(path, value));
        }
    }

    private void applyFormValues(PartListing listing, ListingForm form) {
        listing.setTitle(form.getTitle().trim());
        listing.setBrand(form.getBrand().trim());
        listing.setModel(form.getModel().trim());
        listing.setVehicleYear(form.getVehicleYear());
        listing.setPartCategory(form.getPartCategory().trim());
        listing.setDescription(form.getDescription().trim());
        listing.setPrice(form.getPrice());
        listing.setCondition(form.getCondition());
    }

    private void replaceImages(PartListing listing, List<String> storedImages) {
        if (storedImages.isEmpty()) {
            return;
        }

        listing.getImages().clear();
        listing.setImageFilename(storedImages.getFirst());
        for (int i = 0; i < storedImages.size(); i++) {
            ListingImage image = new ListingImage();
            image.setFilename(storedImages.get(i));
            image.setSortOrder(i);
            listing.addImage(image);
        }
    }
}
