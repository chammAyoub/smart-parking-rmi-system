package com.example.parking_rmi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(
    name = "reservations",
    indexes = {
        @Index(name = "idx_reservation_user_email", columnList = "user_email"),
        @Index(name = "idx_reservation_status", columnList = "status"),
        @Index(name = "idx_reservation_parking_lot", columnList = "parking_lot_id"),
        @Index(name = "idx_reservation_parking_spot", columnList = "parking_spot_id"),
        @Index(name = "idx_reservation_dates", columnList = "start_time, end_time")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // INFORMATIONS UTILISATEUR
    @NotBlank(message = "Le nom de l'utilisateur est requis")
    @Size(min = 3, max = 100, message = "Le nom doit contenir entre 3 et 100 caractères")
    @Column(name = "user_name", nullable = false, length = 100)
    private String userName;

    @NotBlank(message = "L'email de l'utilisateur est requis")
    @Email(message = "Format d'email invalide")
    @Column(name = "user_email", nullable = false, length = 100)
    private String userEmail;

    @NotBlank(message = "Le numéro de téléphone est requis")
    @Pattern(
        regexp = "^(\\+212|0)[5-7]\\d{8}$",
        message = "Format de téléphone invalide (ex: 0612345678 ou +212612345678)"
    )
    @Column(name = "user_phone", nullable = false, length = 20)
    private String userPhone;

    @NotBlank(message = "L'immatriculation du véhicule est requise")
    @Pattern(
        regexp = "^\\d{1,6}-[A-Z]{1,2}-\\d{1,2}$",
        message = "Format d'immatriculation invalide (ex: 12345-A-67)"
    )
    @Column(name = "license_plate", nullable = false, length = 20)
    private String licensePlate;

    // INFORMATIONS RÉSERVATION
    @NotNull(message = "L'heure de début est requise")
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @NotNull(message = "L'heure de fin est requise")
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Min(value = 1, message = "La durée doit être d'au moins 1 heure")
    @Max(value = 24, message = "La durée ne peut pas dépasser 24 heures")
    @Column(name = "duration_hours", nullable = false)
    private Integer durationHours;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private ReservationStatus status = ReservationStatus.PENDING;

    // ✅ FIX: Changed from double to BigDecimal
    @DecimalMin(value = "0.0", message = "Le montant total doit être >= 0")
    @Digits(integer = 10, fraction = 2, message = "Format du montant invalide")
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", length = 20)
    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.UNPAID;

    // CHECK-IN / CHECK-OUT
    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;

    @Column(name = "check_out_time")
    private LocalDateTime checkOutTime;

    // ANNULATION
    @Size(max = 500, message = "La raison d'annulation ne peut pas dépasser 500 caractères")
    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    // TIMESTAMPS
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // RELATIONS
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "parking_lot_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_reservation_parking_lot")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties({"reservations", "spots"})
    private ParkingLot parkingLot;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "parking_spot_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_reservation_parking_spot")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties("currentReservation")
    private ParkingSpot parkingSpot;

    // LIFECYCLE CALLBACKS
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        
        // ✅ FIX: Calculate with BigDecimal
        if ((totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) == 0) 
            && durationHours != null && parkingLot != null) {
            totalAmount = parkingLot.getHourlyRate()
                         .multiply(new BigDecimal(durationHours));
        }
        
        if (endTime == null && startTime != null && durationHours != null) {
            endTime = startTime.plusHours(durationHours);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // BUSINESS METHODS
    public boolean isActive() {
        return status == ReservationStatus.CONFIRMED || status == ReservationStatus.PENDING;
    }

    public boolean isPending() {
        return status == ReservationStatus.PENDING;
    }

    public boolean isConfirmed() {
        return status == ReservationStatus.CONFIRMED;
    }

    public boolean isCancelled() {
        return status == ReservationStatus.CANCELLED;
    }

    public boolean isCompleted() {
        return status == ReservationStatus.COMPLETED;
    }

    public boolean isExpired() {
        return endTime != null && LocalDateTime.now().isAfter(endTime) && !isCompleted();
    }

    public void confirm() {
        if (this.status == ReservationStatus.PENDING) {
            this.status = ReservationStatus.CONFIRMED;
        } else {
            throw new IllegalStateException("Seules les réservations en attente peuvent être confirmées");
        }
    }

    public void cancel(String reason) {
        if (isCancelled() || isCompleted()) {
            throw new IllegalStateException("Cette réservation ne peut pas être annulée");
        }
        
        this.status = ReservationStatus.CANCELLED;
        this.cancellationReason = reason;
        this.cancelledAt = LocalDateTime.now();
    }

    public void complete() {
        if (!isConfirmed()) {
            throw new IllegalStateException("Seules les réservations confirmées peuvent être complétées");
        }
        
        this.status = ReservationStatus.COMPLETED;
        this.checkOutTime = LocalDateTime.now();
    }

    public void checkIn() {
        if (!isConfirmed()) {
            throw new IllegalStateException("Seules les réservations confirmées peuvent effectuer le check-in");
        }
        
        this.checkInTime = LocalDateTime.now();
    }

    public Long getRemainingMinutes() {
        if (endTime == null || isCompleted() || isCancelled()) {
            return 0L;
        }
        
        Duration duration = Duration.between(LocalDateTime.now(), endTime);
        return duration.toMinutes();
    }

    public String getFormattedRemainingTime() {
        Long minutes = getRemainingMinutes();
        
        if (minutes <= 0) {
            return "Expiré";
        }
        
        long hours = minutes / 60;
        long mins = minutes % 60;
        
        if (hours > 0) {
            return String.format("%dh %dmin", hours, mins);
        }
        
        return String.format("%dmin", mins);
    }

    public boolean hasCheckedIn() {
        return checkInTime != null;
    }

    public boolean hasCheckedOut() {
        return checkOutTime != null;
    }

    public Duration getActualDuration() {
        if (checkInTime != null && checkOutTime != null) {
            return Duration.between(checkInTime, checkOutTime);
        }
        return Duration.ZERO;
    }

    // ENUMS
    public enum ReservationStatus {
        PENDING("En attente"),
        CONFIRMED("Confirmée"),
        CANCELLED("Annulée"),
        COMPLETED("Complétée"),
        EXPIRED("Expirée"),
        NO_SHOW("Non présenté");

        private final String displayName;

        ReservationStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum PaymentStatus {
        UNPAID("Non payé"),
        PAID("Payé"),
        REFUNDED("Remboursé"),
        PENDING("En attente");

        private final String displayName;

        PaymentStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}