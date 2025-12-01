package com.example.parking_rmi.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSpotDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String spotNumber;
    private String status;
    private String spotType;
    private Integer floorNumber;
    private String section;
    private Boolean isAccessible;
    private Boolean isCovered;
    private Boolean isElectricCharging;
    private LocalDateTime lastOccupiedAt;
    private Long parkingLotId; // ID only to prevent circular reference
}