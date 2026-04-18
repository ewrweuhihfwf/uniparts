package com.example.parts.service.dto;

import com.example.parts.domain.PartListing;
import com.example.parts.domain.PartCondition;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import org.springframework.web.multipart.MultipartFile;

public class ListingForm {

    @NotBlank(message = "სათაური სავალდებულოა")
    @Size(min = 4, max = 120, message = "სათაური უნდა იყოს 4-120 სიმბოლო")
    private String title;

    @NotBlank(message = "ბრენდი სავალდებულოა")
    @Size(min = 2, max = 80, message = "ბრენდი უნდა იყოს 2-80 სიმბოლო")
    private String brand;

    @NotBlank(message = "მოდელი სავალდებულოა")
    @Size(min = 2, max = 80, message = "მოდელი უნდა იყოს 2-80 სიმბოლო")
    private String model;

    @NotNull(message = "წელი სავალდებულოა")
    @Min(value = 1950, message = "წელი უნდა იყოს 1950-2035 დიაპაზონში")
    @Max(value = 2035, message = "წელი უნდა იყოს 1950-2035 დიაპაზონში")
    private Integer vehicleYear;

    @NotBlank(message = "კატეგორია სავალდებულოა")
    @Size(min = 2, max = 80, message = "კატეგორია უნდა იყოს 2-80 სიმბოლო")
    private String partCategory;

    @NotBlank(message = "აღწერა სავალდებულოა")
    @Size(min = 3, max = 1500, message = "აღწერა უნდა იყოს 3-1500 სიმბოლო")
    private String description;

    @NotNull(message = "ფასი სავალდებულოა")
    @DecimalMin(value = "1.0", message = "ფასი უნდა იყოს მინიმუმ 1 ლარი")
    private BigDecimal price;

    @NotNull(message = "მდგომარეობა სავალდებულოა")
    private PartCondition condition;

    private MultipartFile[] images;

    public static ListingForm from(PartListing listing) {
        ListingForm form = new ListingForm();
        form.setTitle(listing.getTitle());
        form.setBrand(listing.getBrand());
        form.setModel(listing.getModel());
        form.setVehicleYear(listing.getVehicleYear());
        form.setPartCategory(listing.getPartCategory());
        form.setDescription(listing.getDescription());
        form.setPrice(listing.getPrice());
        form.setCondition(listing.getCondition());
        return form;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPartCategory() {
        return partCategory;
    }

    public Integer getVehicleYear() {
        return vehicleYear;
    }

    public void setVehicleYear(Integer vehicleYear) {
        this.vehicleYear = vehicleYear;
    }

    public void setPartCategory(String partCategory) {
        this.partCategory = partCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public PartCondition getCondition() {
        return condition;
    }

    public void setCondition(PartCondition condition) {
        this.condition = condition;
    }

    public MultipartFile[] getImages() {
        return images;
    }

    public void setImages(MultipartFile[] images) {
        this.images = images;
    }
}
