package com.example.parts.controller;

import com.example.parts.config.PartsProperties;
import com.example.parts.domain.PartCondition;
import com.example.parts.service.PartListingService;
import com.example.parts.service.VehicleCatalogService;
import com.example.parts.service.dto.ListingFilter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final PartListingService partListingService;
    private final PartsProperties partsProperties;
    private final VehicleCatalogService vehicleCatalogService;

    public HomeController(PartListingService partListingService,
                          PartsProperties partsProperties,
                          VehicleCatalogService vehicleCatalogService) {
        this.partListingService = partListingService;
        this.partsProperties = partsProperties;
        this.vehicleCatalogService = vehicleCatalogService;
    }

    @GetMapping("/")
    public String index(@ModelAttribute("filter") ListingFilter filter, Model model) {
        boolean hasFilters = filter != null && !filter.isEmpty();
        preparePageModel(model, filter, "/", hasFilters ? "ფილტრის შედეგები" : "ახალი განცხადებები",
                hasFilters ? "შედეგები შენს მითითებულ ფილტრებზე." : "პოპულარული და ახლახან დამატებული ნაწილები.",
                hasFilters ? partListingService.search(filter) : partListingService.getRecentListings(partsProperties.getHomeItemsLimit()));
        return "index";
    }

    @GetMapping("/ads")
    public String ads(@ModelAttribute("filter") ListingFilter filter, Model model) {
        boolean hasFilters = filter != null && !filter.isEmpty();
        preparePageModel(model, filter, "/ads", hasFilters ? "ფილტრის შედეგები" : "ყველა განცხადება",
                hasFilters ? "შედეგები შენს მითითებულ ფილტრებზე." : "ყველა განცხადება ერთ გვერდზე.",
                partListingService.search(filter));
        return "index";
    }

    @ModelAttribute("priceOptions")
    public List<BigDecimal> priceOptions() {
        return List.of(
                new BigDecimal("50"),
                new BigDecimal("100"),
                new BigDecimal("200"),
                new BigDecimal("300"),
                new BigDecimal("500"),
                new BigDecimal("1000"),
                new BigDecimal("2000"),
                new BigDecimal("5000")
        );
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

    @ModelAttribute("categoryOptions")
    public List<String> categoryOptions() {
        return partListingService.getAvailableCategories();
    }

    @ModelAttribute("conditionOptions")
    public List<PartCondition> conditionOptions() {
        return partListingService.getAvailableConditions();
    }

    private void preparePageModel(Model model,
                                  ListingFilter filter,
                                  String searchAction,
                                  String sectionTitle,
                                  String sectionDescription,
                                  List<?> listings) {
        model.addAttribute("filter", filter);
        model.addAttribute("hasKeyword", filter != null && !filter.isEmpty());
        model.addAttribute("searchAction", searchAction);
        model.addAttribute("sectionTitle", sectionTitle);
        model.addAttribute("sectionDescription", sectionDescription);
        model.addAttribute("listings", listings);
    }
}
