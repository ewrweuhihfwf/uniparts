package com.example.parts.controller;

import com.example.parts.domain.AppUser;
import com.example.parts.domain.PartCondition;
import com.example.parts.domain.PartListing;
import com.example.parts.service.AppUserService;
import com.example.parts.service.PartListingService;
import com.example.parts.service.VehicleCatalogService;
import com.example.parts.service.dto.ListingForm;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ListingController {

    private final PartListingService partListingService;
    private final AppUserService appUserService;
    private final VehicleCatalogService vehicleCatalogService;

    public ListingController(PartListingService partListingService,
                             AppUserService appUserService,
                             VehicleCatalogService vehicleCatalogService) {
        this.partListingService = partListingService;
        this.appUserService = appUserService;
        this.vehicleCatalogService = vehicleCatalogService;
    }

    @ModelAttribute("conditions")
    public PartCondition[] conditions() {
        return Arrays.stream(PartCondition.values()).toArray(PartCondition[]::new);
    }

    @ModelAttribute("brandOptions")
    public List<String> brandOptions() {
        return vehicleCatalogService.getBrands();
    }

    @ModelAttribute("modelOptions")
    public List<String> modelOptions() {
        return vehicleCatalogService.getAllModels();
    }

    @ModelAttribute("brandModelMap")
    public Map<String, List<String>> brandModelMap() {
        return vehicleCatalogService.getBrandModelMap();
    }

    @ModelAttribute("yearOptions")
    public List<Integer> yearOptions() {
        return vehicleCatalogService.getYearOptions();
    }

    @GetMapping("/sell")
    public String createForm(Model model) {
        if (!model.containsAttribute("listingForm")) {
            model.addAttribute("listingForm", new ListingForm());
        }
        prepareListingFormPage(model, null);
        return "listing-form";
    }

    @PostMapping("/sell")
    public String createListing(@Valid @ModelAttribute("listingForm") ListingForm listingForm,
                                BindingResult bindingResult,
                                Authentication authentication,
                                Model model) {
        if (bindingResult.hasErrors()) {
            prepareListingFormPage(model, null);
            return "listing-form";
        }

        try {
            AppUser seller = currentUser(authentication);
            PartListing savedListing = partListingService.createListing(listingForm, seller);
            return "redirect:/ads/" + savedListing.getId() + "?created";
        } catch (IllegalArgumentException ex) {
            bindingResult.reject("listing.error", ex.getMessage());
            prepareListingFormPage(model, null);
            return "listing-form";
        }
    }

    @GetMapping("/ads/{id}/edit")
    public String editForm(@PathVariable Long id, Authentication authentication, Model model) {
        AppUser seller = currentUser(authentication);
        PartListing listing;
        try {
            listing = partListingService.getOwnedListing(id, seller);
        } catch (IllegalArgumentException ex) {
            return "redirect:/ads?notFound";
        } catch (IllegalStateException ex) {
            return "redirect:/ads/" + id + "?editError";
        }

        if (!model.containsAttribute("listingForm")) {
            model.addAttribute("listingForm", ListingForm.from(listing));
        }
        prepareListingFormPage(model, listing);
        return "listing-form";
    }

    @PostMapping("/ads/{id}/edit")
    public String updateListing(@PathVariable Long id,
                                @Valid @ModelAttribute("listingForm") ListingForm listingForm,
                                BindingResult bindingResult,
                                Authentication authentication,
                                Model model) {
        AppUser seller = currentUser(authentication);
        PartListing listing;
        try {
            listing = partListingService.getOwnedListing(id, seller);
        } catch (IllegalArgumentException ex) {
            return "redirect:/ads?notFound";
        } catch (IllegalStateException ex) {
            return "redirect:/ads/" + id + "?editError";
        }

        if (bindingResult.hasErrors()) {
            prepareListingFormPage(model, listing);
            return "listing-form";
        }

        try {
            PartListing updatedListing = partListingService.updateListing(id, listingForm, seller);
            return "redirect:/ads/" + updatedListing.getId() + "?updated";
        } catch (IllegalArgumentException ex) {
            bindingResult.reject("listing.error", ex.getMessage());
            prepareListingFormPage(model, listing);
            return "listing-form";
        } catch (IllegalStateException ex) {
            return "redirect:/ads/" + id + "?editError";
        }
    }

    @GetMapping("/ads/{id}")
    public String viewListing(@PathVariable Long id, Authentication authentication, Model model) {
        PartListing listing;
        try {
            listing = partListingService.getById(id);
        } catch (IllegalArgumentException ex) {
            return "redirect:/ads?notFound";
        }
        model.addAttribute("listing", listing);
        boolean canManageListing = canManageListing(authentication, listing);
        model.addAttribute("canDeleteListing", canManageListing);
        model.addAttribute("canEditListing", canManageListing);
        return "listing-details";
    }

    @PostMapping("/ads/{id}/delete")
    public String deleteListing(@PathVariable Long id, Authentication authentication) {
        AppUser seller = currentUser(authentication);
        try {
            partListingService.deleteListing(id, seller);
            return "redirect:/my-ads?deleted";
        } catch (IllegalStateException ex) {
            return "redirect:/ads/" + id + "?deleteError";
        } catch (IllegalArgumentException ex) {
            return "redirect:/my-ads?deleteError";
        }
    }

    @GetMapping("/my-ads")
    public String myAds(Authentication authentication, Model model) {
        AppUser seller = currentUser(authentication);
        model.addAttribute("listings", partListingService.getBySeller(seller));
        return "my-listings";
    }

    private AppUser currentUser(Authentication authentication) {
        return appUserService.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
    }

    private boolean canManageListing(Authentication authentication, PartListing listing) {
        if (authentication == null) {
            return false;
        }

        return appUserService.findByEmail(authentication.getName())
                .map(user -> user.getId().equals(listing.getSeller().getId()))
                .orElse(false);
    }

    private void prepareListingFormPage(Model model, PartListing listing) {
        boolean isEditMode = listing != null;
        model.addAttribute("isEditMode", isEditMode);
        model.addAttribute("pageTitle", isEditMode ? "რედაქტირება" : "ახალი განცხადება");
        model.addAttribute("headerSubtitle", isEditMode ? "განცხადების რედაქტირება" : "ახალი განცხადება");
        model.addAttribute("formKicker", isEditMode ? "რედაქტირება" : "ახალი განცხადება");
        model.addAttribute("formTitle", isEditMode ? "განცხადების რედაქტირება" : "განცხადების დამატება");
        model.addAttribute("formDescription", isEditMode
                ? "შეცვალე მონაცემები და სურვილის შემთხვევაში ატვირთე ახალი ფოტოები ძველების ჩასანაცვლებლად."
                : "აირჩიე ავტომობილის ბრენდი, მოდელი და წელი, შემდეგ დაამატე ფოტოები და ფასი.");
        model.addAttribute("submitLabel", isEditMode ? "შენახვა" : "გამოქვეყნება");
        model.addAttribute("formAction", isEditMode ? "/ads/" + listing.getId() + "/edit" : "/sell");
        model.addAttribute("formViewUrl", isEditMode ? "/ads/" + listing.getId() : null);
        model.addAttribute("existingImageUrls", isEditMode
                ? listing.getAllImageFilenames().stream().map(filename -> "/uploads/" + filename).toList()
                : List.of());
    }
}
