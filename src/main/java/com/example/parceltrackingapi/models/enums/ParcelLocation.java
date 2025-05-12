package com.example.parceltrackingapi.models.enums;

import lombok.Getter;

@Getter
public enum ParcelLocation {
    POLAND("Warsaw, Poland"),
    GERMANY("Berlin, Germany"),
    UNKNOWN("Unknown");

    private final String displayLocation;

    ParcelLocation(String displayLocation) {
        this.displayLocation = displayLocation;
    }
}
