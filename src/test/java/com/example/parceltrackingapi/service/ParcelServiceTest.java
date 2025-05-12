package com.example.parceltrackingapi.service;

import com.example.parceltrackingapi.dto.ParcelResponseDto;
import com.example.parceltrackingapi.exception.ParcelAlreadyExistsException;
import com.example.parceltrackingapi.models.Parcel;
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
        Optional<ParcelResponseDto> result = parcelService.trackParcel("123456", "user123");

        assertTrue(result.isPresent());
        ParcelResponseDto dto = result.get();
        assertEquals("123456", dto.getTrackingNumber());
        assertEquals("user123", dto.getUserId());
        assertEquals("In Transit", dto.getStatus());
    }

    @Test
    void trackParcelShouldReturnEmptyWhenTrackingNumberDoesNotExist() {
        when(parcelRepository.getParcel("000000")).thenReturn(null);

        Optional<ParcelResponseDto> result = parcelService.trackParcel("000000", "user123");

        assertFalse(result.isPresent());
    }

    @Test
    void trackParcelShouldReturnEmptyWhenUserIdDoesNotMatchParcelOwner() {
        Optional<ParcelResponseDto> result = parcelService.trackParcel("123456", "wrongUser");

        assertFalse(result.isPresent());
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
    void sendParcelShouldThrowParcelAlreadyExistsExceptionWhenTrackingNumberAlreadyExists() {
        String trackingNumber = "duplicate123";
        Parcel existingParcel = new Parcel("userX", trackingNumber, "In Transit", "Unknown", "Unknown", "locker1", "small");

        when(parcelRepository.getParcel(trackingNumber)).thenReturn(existingParcel);

        ParcelAlreadyExistsException exception = assertThrows(ParcelAlreadyExistsException.class, () ->
                parcelService.sendParcel("userY", trackingNumber, "locker1", "small")
        );
        assertTrue(exception.getMessage().contains("Parcel with tracking number " + trackingNumber + " already exists"));
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

    @Test
    void sendParcelShouldThrowExceptionWhenTrackingNumberIsEmpty() {
        String trackingNumber = "";
        when(parcelRepository.getParcel(trackingNumber)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                parcelService.sendParcel("user123", trackingNumber, "locker1", "medium")
        );
        assertTrue(exception.getMessage().contains("Tracking number cannot be null or empty"));
    }


    @Test
    void sendParcelShouldThrowExceptionWhenUserIdIsEmpty() {
        String trackingNumber = "userIDEmpty";
        when(parcelRepository.getParcel(trackingNumber)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                parcelService.sendParcel("", trackingNumber, "locker1", "medium")
        );
        assertTrue(exception.getMessage().contains("User ID cannot be null or empty"));
    }
}
