package com.example.parking_rmi.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingSpot implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Long id;

    private String spotNumber;
    private SpotStatus status = SpotStatus.AVAILABLE;
    private SpotType spotType = SpotType.STANDARD;
    private Integer floorNumber = 0;
    private String section;
    private Boolean isAccessible = false;
    private Boolean isCovered = false;
    private Boolean isElectricCharging = false;
    private LocalDateTime lastOccupiedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ============================================
    // RELATIONS
    // ============================================

    /**
     * Relation ManyToOne avec ParkingLot
     * Une place appartient à un parking
     */
    private ParkingLot parkingLot;

    /**
     * Relation OneToOne avec Reservation
     * Une place peut avoir une réservation active
     */

    private Reservation currentReservation;

    // ============================================
    // LIFECYCLE CALLBACKS
    // ============================================

    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ============================================
    // BUSINESS METHODS
    // ============================================

    /**
     * Vérifier si la place est disponible
     */
    public boolean isAvailable() {
        return status == SpotStatus.AVAILABLE;
    }

    /**
     * Vérifier si la place est occupée
     */
    public boolean isOccupied() {
        return status == SpotStatus.OCCUPIED;
    }

    /**
     * Vérifier si la place est réservée
     */
    public boolean isReserved() {
        return status == SpotStatus.RESERVED;
    }

    /**
     * Vérifier si la place est en maintenance
     */
    public boolean isInMaintenance() {
        return status == SpotStatus.MAINTENANCE;
    }

    /**
     * Marquer la place comme occupée
     */
    public void markAsOccupied() {
        this.status = SpotStatus.OCCUPIED;
        this.lastOccupiedAt = LocalDateTime.now();
    }

    /**
     * Marquer la place comme réservée
     */
    public void markAsReserved() {
        if (this.status == SpotStatus.AVAILABLE) {
            this.status = SpotStatus.RESERVED;
        } else {
            throw new IllegalStateException("La place n'est pas disponible pour la réservation");
        }
    }

    /**
     * Marquer la place comme disponible
     */
    public void markAsAvailable() {
        this.status = SpotStatus.AVAILABLE;
    }

    /**
     * Mettre la place en maintenance
     */
    public void markAsMaintenance() {
        this.status = SpotStatus.MAINTENANCE;
    }

    /**
     * Vérifier si la place a des équipements spéciaux
     */
    public boolean hasSpecialEquipment() {
        return isAccessible || isElectricCharging;
    }

    /**
     * Obtenir le nom complet de la place (ex: "Parking Agdal - A1")
     */
    public String getFullName() {
        if (parkingLot != null) {
            return parkingLot.getName() + " - " + spotNumber;
        }
        return spotNumber;
    }

    // ============================================
    // ENUMS
    // ============================================

    /**
     * Enum pour le statut de la place
     */
    public enum SpotStatus {
        AVAILABLE("Disponible"),
        RESERVED("Réservée"),
        OCCUPIED("Occupée"),
        MAINTENANCE("En maintenance");

        private final String displayName;

        SpotStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    /**
     * Enum pour le type de place
     */
    public enum SpotType {
        STANDARD("Standard"),
        COMPACT("Compacte"),
        LARGE("Large"),
        HANDICAPPED("Handicapé"),
        ELECTRIC("Électrique"),
        VIP("VIP");

        private final String displayName;

        SpotType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
