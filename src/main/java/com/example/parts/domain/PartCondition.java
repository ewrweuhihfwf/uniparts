package com.example.parts.domain;

public enum PartCondition {
    NEW("ახალი"),
    USED("მეორადი"),
    REPLACEMENT("შემცვლელი");

    private final String label;

    PartCondition(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
