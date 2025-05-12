package com.example.parceltrackingapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Parcel {
    private String  userId;
    private String trackingNumber;
    private String status;
    private String location;
    private String estimatedDelivery;
    private String lockerId;
    private String packageSize;
}
