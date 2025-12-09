package com.example.parking_rmi.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "parking_lots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class ParkingLot implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom du parking est requis")
    @Size(min = 3, max = 100, message = "Le nom doit contenir entre 3 et 100 caractères")
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @NotBlank(message = "L'adresse est requise")
    @Size(max = 255, message = "L'adresse ne peut pas dépasser 255 caractères")
    @Column(nullable = false)
    private String address;

    @NotBlank(message = "La ville est requise")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String city;

    @NotNull(message = "La latitude est requise")
    @DecimalMin(value = "-90.0", message = "La latitude doit être >= -90")
    @DecimalMax(value = "90.0", message = "La latitude doit être <= 90")
    @Column(nullable = false, precision = 10, scale = 8)
    private BigDecimal latitude;

    @NotNull(message = "La longitude est requise")
    @DecimalMin(value = "-180.0", message = "La longitude doit être >= -180")
    @DecimalMax(value = "180.0", message = "La longitude doit être <= 180")
    @Column(nullable = false, precision = 11, scale = 8)
    private BigDecimal longitude;

    @Min(value = 1, message = "Le nombre total de places doit être au moins 1")
    @Max(value = 1000, message = "Le nombre total de places ne peut pas dépasser 1000")
    @Column(name = "total_spots", nullable = false)
    private Integer totalSpots;

    @Min(value = 0, message = "Le nombre de places disponibles ne peut pas être négatif")
    @Column(name = "available_spots", nullable = false)
    private Integer availableSpots;

    @NotBlank(message = "L'hôte RMI est requis")
    @Column(name = "rmi_host", nullable = false, length = 50)
    private String rmiHost;

    @Min(value = 1024, message = "Le port RMI doit être >= 1024")
    @Max(value = 65535, message = "Le port RMI doit être <= 65535")
    @Column(name = "rmi_port", nullable = false)
    private Integer rmiPort;

    @NotBlank(message = "Le nom du service RMI est requis")
    @Column(name = "rmi_service_name", nullable = false, unique = true, length = 100)
    private String rmiServiceName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private ParkingStatus status = ParkingStatus.ACTIVE;

    // ✅ FIX: Changed from Double to BigDecimal
    @DecimalMin(value = "0.0", message = "Le tarif horaire doit être >= 0")
    @Column(name = "hourly_rate", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal hourlyRate = new BigDecimal("10.00");

    @Column(name = "opening_time", length = 5)
    @Builder.Default
    private String openingTime = "00:00";

    @Column(name = "closing_time", length = 5)
    @Builder.Default
    private String closingTime = "23:59";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ============================================
    // RELATIONS
    // ============================================

    @OneToMany(
        mappedBy = "parkingLot",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private List<ParkingSpot> spots = new ArrayList<>();

    @OneToMany(
        mappedBy = "parkingLot",
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY
    )
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private List<Reservation> reservations = new ArrayList<>();

    // ============================================
    // LIFECYCLE CALLBACKS
    // ============================================

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