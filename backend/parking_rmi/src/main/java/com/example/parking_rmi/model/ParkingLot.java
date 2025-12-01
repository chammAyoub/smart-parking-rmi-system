package com.example.parking_rmi.model;

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
public class ParkingLot implements Serializable {
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
    private ParkingStatus status = ParkingStatus.ACTIVE;
    private BigDecimal hourlyRate = new BigDecimal("10.00");
    private String openingTime = "00:00";
    private String closingTime = "23:59";

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ============================================
    // RELATIONS
    // ============================================
    private List<ParkingSpot> spots = new ArrayList<>();

    private List<Reservation> reservations = new ArrayList<>();

    // ============================================
    // LIFECYCLE CALLBACKS
    // ============================================
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (availableSpots == null) {
            availableSpots = totalSpots;
        }
    }

    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ============================================
    // BUSINESS METHODS
    // ============================================

    public void decrementAvailableSpots() {
        if (availableSpots > 0) {
            availableSpots--;

            if (availableSpots == 0) {
                status = ParkingStatus.FULL;
            }
        }
    }

    public void incrementAvailableSpots() {
        if (availableSpots < totalSpots) {
            availableSpots++;

            if (status == ParkingStatus.FULL && availableSpots > 0) {
                status = ParkingStatus.ACTIVE;
            }
        }
    }

    public boolean isOpen() {
        return status == ParkingStatus.ACTIVE;
    }

    public boolean hasAvailableSpots() {
        return availableSpots > 0;
    }

    public double getOccupancyRate() {
        if (totalSpots == 0) {
            return 0.0;
        }
        return ((totalSpots - availableSpots) * 100.0) / totalSpots;
    }

    public int getOccupiedSpots() {
        return totalSpots - availableSpots;
    }

    public void addSpot(ParkingSpot spot) {
        spots.add(spot);
        spot.setParkingLot(this);
    }

    public void removeSpot(ParkingSpot spot) {
        spots.remove(spot);
        spot.setParkingLot(null);
    }

    // ============================================
    // ENUMS
    // ============================================

    public enum ParkingStatus {
        ACTIVE("Actif"),
        MAINTENANCE("En maintenance"),
        CLOSED("Fermé"),
        FULL("Complet");

        private final String displayName;

        ParkingStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
