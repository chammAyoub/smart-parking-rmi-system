package com.example.parking_rmi.model;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
     private static final long serialVersionUID = 1L;

    private Long id;

    // INFORMATIONS UTILISATEUR
  
    private String userName;
    private String userEmail;
    private String userPhone;
    private String licensePlate;

    // INFORMATIONS RÉSERVATION
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer durationHours;
    private ReservationStatus status = ReservationStatus.PENDING;

    // ✅ FIX: Changed from double to BigDecimal
    private BigDecimal totalAmount = BigDecimal.ZERO;
    private PaymentStatus paymentStatus = PaymentStatus.UNPAID;

    // CHECK-IN / CHECK-OUT

    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    // ANNULATION
    private String cancellationReason;
    private LocalDateTime cancelledAt;

    // TIMESTAMPS
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // RELATIONS
    private ParkingLot parkingLot;
    private ParkingSpot parkingSpot;

    // LIFECYCLE CALLBACKS
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
