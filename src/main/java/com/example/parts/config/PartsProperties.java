package com.example.parts.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "parts")
public class PartsProperties {

    @NotBlank
    private String marketplaceName;

    @NotBlank
    private String contactPhone;

    @NotBlank
    private String contactEmail;

    @NotBlank
    private String officeAddress;

    @NotBlank
    private String workingHours;

    @NotBlank
    private String uploadDir;

    @Min(1)
    private int homeItemsLimit;

    public String getMarketplaceName() {
        return marketplaceName;
    }

    public void setMarketplaceName(String marketplaceName) {
        this.marketplaceName = marketplaceName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public int getHomeItemsLimit() {
        return homeItemsLimit;
    }

    public void setHomeItemsLimit(int homeItemsLimit) {
        this.homeItemsLimit = homeItemsLimit;
    }
}
