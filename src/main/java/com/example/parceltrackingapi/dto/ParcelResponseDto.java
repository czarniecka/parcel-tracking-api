package com.example.parceltrackingapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParcelResponseDto {
    private String userId;
    private String trackingNumber;
    private String status;
    private String location;
    private String estimatedDelivery;
}
