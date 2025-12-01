package com.example.parking_rmi.dto;


import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ParkingLotDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String address;
    private String city;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Integer totalSpots;
    private Integer availableSpots;
    private String rmiHost;
    private Integer rmiPort;
    private String rmiServiceName;
    private String status; 
    private BigDecimal hourlyRate;
    private String openingTime;
    private String closingTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // These lists MUST be standard ArrayLists, never Hibernate Proxies
    private List<ParkingSpotDTO> parkingSpot = new ArrayList<>();
    private List<ReservationDTO> reservations = new ArrayList<>();
}