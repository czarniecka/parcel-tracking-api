package com.example.parceltrackingapi.model;

import lombok.Getter;

@Getter
public enum ParcelStatus {
    IN_TRANSIT("In Transit"),
    DELIVERED("Delivered"),
    PENDING("Pending"),
    UNKNOWN("Unknown");

    private final String displayName;

    ParcelStatus(String displayName) {
        this.displayName = displayName;
    }
}
