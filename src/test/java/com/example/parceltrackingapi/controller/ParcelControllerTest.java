package com.example.parceltrackingapi.controller;

import com.example.parceltrackingapi.dto.ParcelResponseDto;
import com.example.parceltrackingapi.dto.ParcelRequestDto;
import com.example.parceltrackingapi.exception.ParcelAlreadyExistsException;
import com.example.parceltrackingapi.models.Parcel;
import com.example.parceltrackingapi.models.enums.ParcelStatus;
import com.example.parceltrackingapi.service.ParcelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParcelControllerTest {

    @Mock
    private ParcelService parcelService;

    @InjectMocks
    private ParcelController parcelController;

    private ParcelResponseDto parcelResponseDto;
    private ParcelRequestDto parcelRequestDto;
    private Parcel parcel;

    @BeforeEach
    void setUp() {
        parcelResponseDto = new ParcelResponseDto();
        parcelResponseDto.setTrackingNumber("track123");
        parcelResponseDto.setStatus(ParcelStatus.IN_TRANSIT.getDisplayName());

        parcelRequestDto = new ParcelRequestDto();
        parcelRequestDto.setUserId("user123");
        parcelRequestDto.setTrackingNumber("track456");
        parcelRequestDto.setLockerId("locker789");
        parcelRequestDto.setPackageSize("medium");

        parcel = new Parcel();
        parcel.setTrackingNumber("track456");
    }

    @Test
    void trackParcelShouldReturnParcelWhenExists() {
        when(parcelService.trackParcel(anyString(), anyString()))
                .thenReturn(Optional.of(parcelResponseDto));

        ResponseEntity<ParcelResponseDto> response = parcelController.trackParcel("user123", "track123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("track123", response.getBody().getTrackingNumber());
    }

    @Test
    void trackParcelShouldReturnNotFoundWhenParcelDoesNotExist() {
        when(parcelService.trackParcel(anyString(), anyString()))
                .thenReturn(Optional.empty());

        ResponseEntity<ParcelResponseDto> response = parcelController.trackParcel("user123", "track999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void trackParcelShouldReturnNotFoundWhenUserIDIsNotCorrect() {
        when(parcelService.trackParcel(anyString(), anyString()))
                .thenReturn(Optional.empty());

        ResponseEntity<ParcelResponseDto> response = parcelController.trackParcel("_user123", "track123");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void sendParcelShouldReturnSuccessResponseWhenParcelIsSent() {
        when(parcelService.sendParcel(
                anyString(), anyString(), anyString(), anyString()))
                .thenReturn(parcel);

        ResponseEntity<Map<String, String>> response = parcelController.sendParcel(parcelRequestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Package sent successfully", response.getBody().get("message"));
        assertEquals("track456", response.getBody().get("trackingNumber"));
    }

    @Test
    void sendParcelShouldThrowExceptionWhenParcelAlreadyExists() {
        when(parcelService.sendParcel(anyString(), anyString(), anyString(), anyString()))
                .thenThrow(new ParcelAlreadyExistsException("Parcel with tracking number track456 already exists"));

        ParcelAlreadyExistsException thrown = assertThrows(ParcelAlreadyExistsException.class, () ->
                parcelController.sendParcel(parcelRequestDto));

        assertEquals("Parcel with tracking number track456 already exists", thrown.getMessage());
    }

    @Test
    void sendParcelShouldThrowIllegalArgumentExceptionWhenUserIdIsNull() {
        when(parcelService.sendParcel(anyString(), anyString(), anyString(), anyString()))
                .thenThrow(new IllegalArgumentException("User ID cannot be null or empty"));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->
                parcelController.sendParcel(parcelRequestDto));

        assertEquals("User ID cannot be null or empty", thrown.getMessage());
    }

    @Test
    void sendParcelShouldThrowRuntimeExceptionWhenUnexpectedErrorOccurs() {
        when(parcelService.sendParcel(anyString(), anyString(), anyString(), anyString()))
                .thenThrow(new RuntimeException("An unexpected error occurred"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                parcelController.sendParcel(parcelRequestDto));

        assertEquals("An unexpected error occurred", thrown.getMessage());
    }
}
