package com.example.parceltrackingapi.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParcelRequestDto {
    @NotNull(message = "User ID cannot be null")
    private String userId;
    @NotNull(message = "Tracking number cannot be null")
    private String trackingNumber;
    @NotNull(message = "Locker ID cannot be null")
    private String lockerId;
    @NotNull(message = "Package size cannot be null")
    private String packageSize;
}
