package com.example.parceltrackingapi.service;

import com.example.parceltrackingapi.dto.ParcelDto;
import com.example.parceltrackingapi.model.Parcel;
import com.example.parceltrackingapi.repository.ParcelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ParcelServiceTest {

    @Mock
    private ParcelRepository parcelRepository;

    @InjectMocks
    private ParcelService parcelService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Parcel sampleParcel = new Parcel(
                "user123",
                "123456",
                "In Transit",
                "Warsaw, Poland",
                "2023-10-15T12:00:00Z",
                "locker1",
                "medium"
        );

        when(parcelRepository.getParcel("123456")).thenReturn(sampleParcel);
    }

    @Test
    void trackParcelShouldReturnParcelWhenParcelExistsAndUserIdMatches() {
        Optional<ParcelDto> result = parcelService.trackParcel("123456", "user123");

        assertTrue(result.isPresent());
        ParcelDto dto = result.get();
        assertEquals("123456", dto.getTrackingNumber());
        assertEquals("user123", dto.getUserId());
        assertEquals("In Transit", dto.getStatus());
    }

    @Test
    void trackParcelShouldReturnEmptyWhenTrackingNumberDoesNotExist() {
        when(parcelRepository.getParcel("000000")).thenReturn(null);

        Optional<ParcelDto> result = parcelService.trackParcel("000000", "user123");

        assertFalse(result.isPresent());
    }

    @Test
    void trackParcelShouldReturnEmptyWhenUserIdDoesNotMatchParcelOwner() {
        Optional<ParcelDto> result = parcelService.trackParcel("123456", "wrongUser");

        assertFalse(result.isPresent());
    }

    @Test
    void trackParcelShouldStoreParcelWhenParcelDoesNotExistYet() {
        String trackingNumber = "654321";
        String userId = "user456";
        String lockerId = "locker9";
        String packageSize = "large";

        when(parcelRepository.getParcel(trackingNumber)).thenReturn(null);

        Parcel parcel = parcelService.sendParcel(userId, trackingNumber, lockerId, packageSize);

        assertNotNull(parcel);
        assertEquals(trackingNumber, parcel.getTrackingNumber());

        when(parcelRepository.getParcel(trackingNumber)).thenReturn(parcel);

        Optional<ParcelDto> result = parcelService.trackParcel(trackingNumber, userId);

        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getUserId());
    }

    @Test
    void sendParcelShouldCreateParcelWhenInputIsValidAndTrackingNumberIsUnique() {
        String userId = "user001";
        String trackingNumber = "unique123";
        String lockerId = "lockerX";
        String packageSize = "small";

        when(parcelRepository.getParcel(trackingNumber)).thenReturn(null);

        Parcel result = parcelService.sendParcel(userId, trackingNumber, lockerId, packageSize);

        assertNotNull(result);
        assertEquals(trackingNumber, result.getTrackingNumber());
        verify(parcelRepository).addParcel(any(Parcel.class));
    }

    @Test
    void sendParcelShouldThrowExceptionWhenTrackingNumberAlreadyExists() {
        String trackingNumber = "duplicate123";
        Parcel existingParcel = new Parcel("userX", trackingNumber, "In Transit", "Unknown", "Unknown", "locker1", "small");

        when(parcelRepository.getParcel(trackingNumber)).thenReturn(existingParcel);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                parcelService.sendParcel("userY", trackingNumber, "locker1", "small")
        );
        assertTrue(exception.getMessage().contains("already exists"));
    }

    @Test
    void sendParcelShouldThrowExceptionWhenUserIdIsNull() {
        String trackingNumber = "new123";
        when(parcelRepository.getParcel(trackingNumber)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                parcelService.sendParcel(null, trackingNumber, "locker1", "small")
        );
        assertTrue(exception.getMessage().contains("User ID cannot be null"));
    }

    @Test
    void sendParcelShouldThrowExceptionWhenPackageSizeIsInvalid() {
        String trackingNumber = "invalidSize123";
        when(parcelRepository.getParcel(trackingNumber)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                parcelService.sendParcel("userZ", trackingNumber, "locker1", "gigantic")
        );
        assertTrue(exception.getMessage().contains("Invalid package size"));
    }

    @Test
    void sendParcelShouldThrowExceptionWhenLockerIdIsEmpty() {
        String trackingNumber = "lockerEmpty";
        when(parcelRepository.getParcel(trackingNumber)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                parcelService.sendParcel("userZ", trackingNumber, "", "medium")
        );
        assertTrue(exception.getMessage().contains("Locker ID cannot be null or empty"));
    }
}
