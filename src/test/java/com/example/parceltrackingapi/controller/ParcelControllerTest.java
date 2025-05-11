package com.example.parceltrackingapi.controller;

import com.example.parceltrackingapi.dto.ParcelDto;
import com.example.parceltrackingapi.dto.ParcelRequestDto;
import com.example.parceltrackingapi.model.Parcel;
import com.example.parceltrackingapi.model.ParcelStatus;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParcelControllerTest {

    @Mock
    private ParcelService parcelService;

    @InjectMocks
    private ParcelController parcelController;

    private ParcelDto parcelDto;
    private ParcelRequestDto parcelRequestDto;
    private Parcel parcel;

    @BeforeEach
    void setUp() {
        parcelDto = new ParcelDto();
        parcelDto.setTrackingNumber("track123");
        parcelDto.setStatus(ParcelStatus.IN_TRANSIT.getDisplayName());

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
                .thenReturn(Optional.of(parcelDto));

        ResponseEntity<ParcelDto> response = parcelController.trackParcel("user123", "track123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("track123", response.getBody().getTrackingNumber());
    }

    @Test
    void trackParcelShouldReturnNotFoundWhenParcelDoesNotExist() {
        when(parcelService.trackParcel(anyString(), anyString()))
                .thenReturn(Optional.empty());

        ResponseEntity<ParcelDto> response = parcelController.trackParcel("user123", "track999");

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
}
