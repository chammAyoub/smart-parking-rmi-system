package com.example.parking_rmi.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.parking_rmi.model.Reservation;
import com.example.parking_rmi.model.Reservation.ReservationStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository pour la gestion des réservations
 * 
 * @author Omar - Backend Core
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * Trouver toutes les réservations d'un utilisateur
     */
    List<Reservation> findByUserEmail(String userEmail);

    /**
     * Trouver les réservations actives d'un utilisateur
     */
    @Query("SELECT r FROM Reservation r WHERE r.userEmail = :email " +
           "AND r.status IN ('PENDING', 'CONFIRMED') " +
           "ORDER BY r.startTime DESC")
    List<Reservation> findActiveReservationsByUserEmail(@Param("email") String email);

    /**
     * Trouver toutes les réservations d'un parking
     */
    List<Reservation> findByParkingLotId(Long parkingLotId);

    /**
     * Trouver toutes les réservations d'une place
     */
    List<Reservation> findByParkingSpotId(Long parkingSpotId);

    /**
     * Trouver la réservation active d'une place
     */
    @Query("SELECT r FROM Reservation r WHERE r.parkingSpot.id = :spotId " +
           "AND r.status IN ('PENDING', 'CONFIRMED') " +
           "AND r.endTime > :now")
    Optional<Reservation> findActiveReservationBySpotId(@Param("spotId") Long spotId,
                                                         @Param("now") LocalDateTime now);

    /**
     * Trouver les réservations par statut
     */
    List<Reservation> findByStatus(ReservationStatus status);

    /**
     * Trouver les réservations par statut dans un parking
     */
    List<Reservation> findByParkingLotIdAndStatus(Long parkingLotId, ReservationStatus status);

    /**
     * Trouver les réservations par période
     */
    @Query("SELECT r FROM Reservation r WHERE r.startTime BETWEEN :startDate AND :endDate " +
           "ORDER BY r.startTime")
    List<Reservation> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);

    /**
     * Trouver les réservations qui se chevauchent pour une place
     */
    @Query("SELECT r FROM Reservation r WHERE r.parkingSpot.id = :spotId " +
           "AND r.status IN ('PENDING', 'CONFIRMED') " +
           "AND ((r.startTime BETWEEN :startTime AND :endTime) " +
           "OR (r.endTime BETWEEN :startTime AND :endTime) " +
           "OR (:startTime BETWEEN r.startTime AND r.endTime))")
    List<Reservation> findOverlappingReservations(@Param("spotId") Long spotId,
                                                   @Param("startTime") LocalDateTime startTime,
                                                   @Param("endTime") LocalDateTime endTime);

    /**
     * Trouver les réservations expirées
     */
    @Query("SELECT r FROM Reservation r WHERE r.status = 'CONFIRMED' " +
           "AND r.endTime < :now")
    List<Reservation> findExpiredReservations(@Param("now") LocalDateTime now);

    /**
     * Compter les réservations par statut
     */
    Long countByStatus(ReservationStatus status);

    /**
     * Compter les réservations d'un parking
     */
    Long countByParkingLotId(Long parkingLotId);

    /**
     * Compter les réservations d'un utilisateur
     */
    Long countByUserEmail(String userEmail);

    /**
     * Calculer le revenu total d'un parking
     */
    @Query("SELECT SUM(r.totalAmount) FROM Reservation r " +
           "WHERE r.parkingLot.id = :parkingLotId " +
           "AND r.status = 'COMPLETED'")
    Double getTotalRevenueByParkingLotId(@Param("parkingLotId") Long parkingLotId);

    /**
     * Calculer le revenu total par période
     */
    @Query("SELECT SUM(r.totalAmount) FROM Reservation r " +
           "WHERE r.status = 'COMPLETED' " +
           "AND r.startTime BETWEEN :startDate AND :endDate")
    Double getTotalRevenueByDateRange(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);

    /**
     * Trouver les réservations récentes
     */
    @Query("SELECT r FROM Reservation r ORDER BY r.createdAt DESC")
    List<Reservation> findRecentReservations();

    /**
     * Vérifier si une place est réservée
     */
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
           "FROM Reservation r WHERE r.parkingSpot.id = :spotId " +
           "AND r.status IN ('PENDING', 'CONFIRMED') " +
           "AND r.endTime > :now")
    boolean isSpotReserved(@Param("spotId") Long spotId, @Param("now") LocalDateTime now);
}