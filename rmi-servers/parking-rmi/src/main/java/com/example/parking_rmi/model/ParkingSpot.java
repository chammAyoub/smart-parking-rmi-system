package com.example.parking_rmi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entity représentant une place de parking individuelle
 * 
 * @author Omar - Backend Core Developer
 */
@Entity
@Table(
    name = "parking_spots",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_spot_number",
            columnNames = {"parking_lot_id", "spot_number"}
        )
    },
    indexes = {
        @Index(name = "idx_spot_status", columnList = "status"),
        @Index(name = "idx_spot_parking_lot", columnList = "parking_lot_id"),
        @Index(name = "idx_spot_type", columnList = "spot_type")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingSpot implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le numéro de place est requis")
    @Size(min = 1, max = 10, message = "Le numéro de place doit contenir entre 1 et 10 caractères")
    @Column(name = "spot_number", nullable = false, length = 10)
    private String spotNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private SpotStatus status = SpotStatus.AVAILABLE;

    @Enumerated(EnumType.STRING)
    @Column(name = "spot_type", nullable = false, length = 20)
    @Builder.Default
    private SpotType spotType = SpotType.STANDARD;

    @Min(value = 0, message = "Le numéro d'étage ne peut pas être négatif")
    @Column(name = "floor_number")
    @Builder.Default
    private Integer floorNumber = 0;

    @Size(max = 10, message = "La section ne peut pas dépasser 10 caractères")
    @Column(name = "section", length = 10)
    private String section;

    @Column(name = "is_accessible")
    @Builder.Default
    private Boolean isAccessible = false;

    @Column(name = "is_covered")
    @Builder.Default
    private Boolean isCovered = false;

    @Column(name = "is_electric_charging")
    @Builder.Default
    private Boolean isElectricCharging = false;

    @Column(name = "last_occupied_at")
    private LocalDateTime lastOccupiedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ============================================
    // RELATIONS
    // ============================================

    /**
     * Relation ManyToOne avec ParkingLot
     * Une place appartient à un parking
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "parking_lot_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_spot_parking_lot")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ParkingLot parkingLot;

    /**
     * Relation OneToOne avec Reservation
     * Une place peut avoir une réservation active
     */
    @OneToOne(
        mappedBy = "parkingSpot",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Reservation currentReservation;

    // ============================================
    // LIFECYCLE CALLBACKS
    // ============================================

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
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