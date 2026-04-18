package com.example.parts.controller;

import com.example.parts.domain.PartListing;
import com.example.parts.service.PartListingService;
import com.example.parts.service.dto.ListingFilter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/listings")
public class ListingRestController {

    private final PartListingService partListingService;

    public ListingRestController(PartListingService partListingService) {
        this.partListingService = partListingService;
    }

    @GetMapping
    public List<ListingResponse> all(@RequestParam(required = false) String keyword,
                                     @RequestParam(required = false) String brand,
                                     @RequestParam(required = false) String model,
                                     @RequestParam(required = false) Integer minYear,
                                     @RequestParam(required = false) Integer maxYear,
                                     @RequestParam(required = false) String partCategory,
                                     @RequestParam(required = false) String condition,
                                     @RequestParam(required = false) BigDecimal minPrice,
                                     @RequestParam(required = false) BigDecimal maxPrice) {
        ListingFilter filter = new ListingFilter();
        filter.setKeyword(keyword);
        filter.setBrand(brand);
        filter.setModel(model);
        filter.setMinYear(minYear);
        filter.setMaxYear(maxYear);
        filter.setPartCategory(partCategory);
        if (condition != null && !condition.isBlank()) {
            filter.setCondition(Enum.valueOf(com.example.parts.domain.PartCondition.class, condition));
        }
        filter.setMinPrice(minPrice);
        filter.setMaxPrice(maxPrice);

        return partListingService.search(filter).stream()
                .map(ListingResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public ListingResponse byId(@PathVariable Long id) {
        return ListingResponse.from(partListingService.getById(id));
    }

    public record ListingResponse(
            Long id,
            String title,
            String brand,
            String model,
            Integer vehicleYear,
            String category,
            String description,
            BigDecimal price,
            String condition,
            String sellerName,
            String imageUrl,
            LocalDateTime createdAt
    ) {
        static ListingResponse from(PartListing listing) {
            return new ListingResponse(
                    listing.getId(),
                    listing.getTitle(),
                    listing.getBrand(),
                    listing.getModel(),
                    listing.getVehicleYear(),
                    listing.getPartCategory(),
                    listing.getDescription(),
                    listing.getPrice(),
                    listing.getCondition().name(),
                    listing.getSeller().getFullName(),
                    listing.getPreviewImageFilename() == null ? "/images/empty-part.svg" : "/uploads/" + listing.getPreviewImageFilename(),
                    listing.getCreatedAt()
            );
        }
    }
}
