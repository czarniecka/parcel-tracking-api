package com.example.parceltrackingapi.service;

import com.example.parceltrackingapi.dto.ParcelResponseDto;
import com.example.parceltrackingapi.exception.ParcelAlreadyExistsException;
import com.example.parceltrackingapi.models.Parcel;
import com.example.parceltrackingapi.models.enums.ParcelLocation;
import com.example.parceltrackingapi.models.enums.ParcelSize;
import com.example.parceltrackingapi.models.enums.ParcelStatus;
import com.example.parceltrackingapi.repository.ParcelRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ParcelService {

    private final ParcelRepository parcelRepository;

    public ParcelService(ParcelRepository parcelRepository) {
        this.parcelRepository = parcelRepository;
    }

    public Optional<ParcelResponseDto> trackParcel(String trackingNumber, String userId) {
        Parcel parcel = parcelRepository.getParcel(trackingNumber);
        if (isParcelAccessible(parcel, userId)) {
            ParcelResponseDto parcelResponseDto = convertToDto(parcel);
            return Optional.of(parcelResponseDto);
        }
        return Optional.empty();
    }

    public Parcel sendParcel(String userId, String trackingNumber, String lockerId, String packageSize) {
        checkIfParcelExistsOrThrow(trackingNumber);
        Parcel parcel = createAndValidateParcel(userId, trackingNumber, lockerId, packageSize);
        parcelRepository.addParcel(parcel);
        return parcel;
    }

    private boolean isParcelAccessible(Parcel parcel, String userId) {
        return parcel != null && parcel.getUserId().equals(userId);
    }

    private ParcelResponseDto convertToDto(Parcel parcel) {
        return new ParcelResponseDto(
                parcel.getUserId(),
                parcel.getTrackingNumber(),
                parcel.getStatus(),
                parcel.getLocation(),
                parcel.getEstimatedDelivery()
        );
    }

    private Parcel createAndValidateParcel(String userId, String trackingNumber, String lockerId, String packageSize) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (trackingNumber == null || trackingNumber.isEmpty()) {
            throw new IllegalArgumentException("Tracking number cannot be null or empty");
        }
        if (lockerId == null || lockerId.isEmpty()) {
            throw new IllegalArgumentException("Locker ID cannot be null or empty");
        }
        if (packageSize == null || packageSize.isEmpty()) {
            throw new IllegalArgumentException("Package size cannot be null or empty");
        }
        if (!isValidPackageSize(packageSize)) {
            throw new IllegalArgumentException("Invalid package size: " + packageSize);
        }
        return new Parcel(userId, trackingNumber, ParcelStatus.IN_TRANSIT.getDisplayName(), ParcelLocation.UNKNOWN.getDisplayLocation(), "Unknown", lockerId, packageSize);
    }

    private boolean isValidPackageSize(String packageSize) {
        for (ParcelSize size : ParcelSize.values()) {
            if (size.getDisplayParcelSize().equalsIgnoreCase(packageSize)) {
                return true;
            }
        }
        return false;
    }

    private void checkIfParcelExistsOrThrow(String trackingNumber) {
        if (parcelRepository.getParcel(trackingNumber) != null) {
            throw new ParcelAlreadyExistsException("Parcel with tracking number " + trackingNumber + " already exists");
        }
    }
}
