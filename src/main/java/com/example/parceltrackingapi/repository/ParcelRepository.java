package com.example.parceltrackingapi.repository;

import com.example.parceltrackingapi.model.Parcel;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ParcelRepository {
    private final Map<String, Parcel> parcelStorage = new HashMap<>();

    @PostConstruct
    public void init() {
        parcelStorage.put("123456", new Parcel(
                "user123",
                "123456",
                "In Transit",
                "Warsaw, Poland",
                "2023-10-15T12:00:00Z",
                "locker1",
                "medium"
        ));

        parcelStorage.put("121212", new Parcel(
                "user456",
                "121212",
                "Delivered",
                "Berlin, Germany",
                "2023-10-14T09:00:00Z",
                "locker3",
                "small"
        ));

        parcelStorage.put("654321", new Parcel(
                "user789",
                "654321",
                "In Transit",
                "London, UK",
                "2023-10-13T17:45:00Z",
                "locker3",
                "large"
        ));
    }

    public Parcel getParcel(String trackingNumber) {
        return parcelStorage.get(trackingNumber);
    }

    public void addParcel(Parcel parcel) {
        parcelStorage.put(parcel.getTrackingNumber(), parcel);
    }
}
