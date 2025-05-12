package com.example.parceltrackingapi.models.enums;

import lombok.Getter;

@Getter
public enum ParcelSize {
    SMALL("small"),
    MEDIUM("medium"),
    LARGE("large"),
    UNKNOWN("unknown");

    private final String displayParcelSize;

    ParcelSize(String displayParcelSize) {
        this.displayParcelSize = displayParcelSize;
    }
}
