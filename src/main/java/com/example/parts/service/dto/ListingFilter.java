package com.example.parts.service.dto;

import com.example.parts.domain.PartCondition;
import java.math.BigDecimal;

public class ListingFilter {

    private String keyword;
    private String brand;
    private String model;
    private Integer minYear;
    private Integer maxYear;
    private String partCategory;
    private PartCondition condition;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    public boolean isEmpty() {
        return isBlank(keyword)
                && isBlank(brand)
                && isBlank(model)
                && minYear == null
                && maxYear == null
                && isBlank(partCategory)
                && condition == null
                && minPrice == null
                && maxPrice == null;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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

    public Integer getMinYear() {
        return minYear;
    }

    public void setMinYear(Integer minYear) {
        this.minYear = minYear;
    }

    public Integer getMaxYear() {
        return maxYear;
    }

    public void setMaxYear(Integer maxYear) {
        this.maxYear = maxYear;
    }

    public String getPartCategory() {
        return partCategory;
    }

    public void setPartCategory(String partCategory) {
        this.partCategory = partCategory;
    }

    public PartCondition getCondition() {
        return condition;
    }

    public void setCondition(PartCondition condition) {
        this.condition = condition;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }
}
