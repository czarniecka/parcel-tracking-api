package com.example.parceltrackingapi.controller;

import com.example.parceltrackingapi.dto.ParcelDto;
import com.example.parceltrackingapi.dto.ParcelRequestDto;
import com.example.parceltrackingapi.model.Parcel;
import com.example.parceltrackingapi.service.ParcelService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ParcelController {
    private final ParcelService parcelService;

    public ParcelController(ParcelService parcelService) {
        this.parcelService = parcelService;
    }

    @GetMapping("/track/{userId}/{trackingNumber}")
    public ResponseEntity<ParcelDto> trackParcel(
            @PathVariable String userId,
            @PathVariable String trackingNumber) {

        Optional<ParcelDto> parcelDtoOpt = parcelService.trackParcel(trackingNumber, userId);
        return parcelDtoOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/packages/send")
    public ResponseEntity<Map<String, String>> sendParcel(
            @Valid @RequestBody ParcelRequestDto parcelRequest) {

        Parcel parcel = parcelService.sendParcel(
                parcelRequest.getUserId(),
                parcelRequest.getTrackingNumber(),
                parcelRequest.getLockerId(),
                parcelRequest.getPackageSize()
        );

        Map<String, String> response = new HashMap<>();
        response.put("message", "Package sent successfully");
        response.put("trackingNumber", parcel.getTrackingNumber());

        return ResponseEntity.ok(response);
    }
}
