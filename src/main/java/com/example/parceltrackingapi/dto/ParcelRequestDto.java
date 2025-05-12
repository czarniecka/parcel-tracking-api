package com.example.parceltrackingapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParcelRequestDto {
    @NotNull(message = "User ID cannot be null")
    @NotBlank(message = "User ID cannot be empty")
    private String userId;
    @NotNull(message = "Tracking number cannot be null")
    @NotBlank(message = "Tracking number cannot be empty")
    private String trackingNumber;
    @NotNull(message = "Locker ID cannot be null")
    @NotBlank(message = "Locker ID cannot be empty")
    private String lockerId;
    @NotNull(message = "Package size cannot be null")
    @NotBlank(message = "Package size cannot be empty")
    private String packageSize;
}
