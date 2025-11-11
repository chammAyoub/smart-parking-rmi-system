package com.example.parking_rmi.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.parking_rmi.model.ParkingSpot;
import com.example.parking_rmi.model.ParkingSpot.SpotStatus;
import com.example.parking_rmi.model.ParkingSpot.SpotType;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour la gestion des places de parking
 * 
 * @author Omar - Backend Core
 */
@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {

    /**
     * Trouver toutes les places d'un parking
     */
    List<ParkingSpot> findByParkingLotId(Long parkingLotId);

    /**
     * Trouver une place par numéro dans un parking
     */
    Optional<ParkingSpot> findByParkingLotIdAndSpotNumber(Long parkingLotId, String spotNumber);

    /**
     * Trouver toutes les places disponibles d'un parking
     */
    @Query("SELECT s FROM ParkingSpot s WHERE s.parkingLot.id = :parkingLotId AND s.status = 'AVAILABLE'")
    List<ParkingSpot> findAvailableSpotsByParkingLotId(@Param("parkingLotId") Long parkingLotId);

    /**
     * Trouver les places par statut
     */
    List<ParkingSpot> findByStatus(SpotStatus status);

    /**
     * Trouver les places par statut dans un parking
     */
    List<ParkingSpot> findByParkingLotIdAndStatus(Long parkingLotId, SpotStatus status);

    /**
     * Trouver les places par type
     */
    List<ParkingSpot> findBySpotType(SpotType spotType);

    /**
     * Trouver les places par type dans un parking
     */
    List<ParkingSpot> findByParkingLotIdAndSpotType(Long parkingLotId, SpotType spotType);

    /**
     * Trouver les places accessibles
     */
    @Query("SELECT s FROM ParkingSpot s WHERE s.parkingLot.id = :parkingLotId AND s.isAccessible = true")
    List<ParkingSpot> findAccessibleSpotsByParkingLotId(@Param("parkingLotId") Long parkingLotId);

    /**
     * Trouver les places avec borne électrique
     */
    @Query("SELECT s FROM ParkingSpot s WHERE s.parkingLot.id = :parkingLotId AND s.isElectricCharging = true")
    List<ParkingSpot> findElectricSpotsByParkingLotId(@Param("parkingLotId") Long parkingLotId);

    /**
     * Compter les places par statut dans un parking
     */
    Long countByParkingLotIdAndStatus(Long parkingLotId, SpotStatus status);

    /**
     * Compter les places disponibles dans un parking
     */
    @Query("SELECT COUNT(s) FROM ParkingSpot s WHERE s.parkingLot.id = :parkingLotId AND s.status = 'AVAILABLE'")
    Long countAvailableSpots(@Param("parkingLotId") Long parkingLotId);

    /**
     * Compter les places occupées dans un parking
     */
    @Query("SELECT COUNT(s) FROM ParkingSpot s WHERE s.parkingLot.id = :parkingLotId AND s.status = 'OCCUPIED'")
    Long countOccupiedSpots(@Param("parkingLotId") Long parkingLotId);

    /**
     * Compter les places réservées dans un parking
     */
    @Query("SELECT COUNT(s) FROM ParkingSpot s WHERE s.parkingLot.id = :parkingLotId AND s.status = 'RESERVED'")
    Long countReservedSpots(@Param("parkingLotId") Long parkingLotId);

    /**
     * Vérifier si une place existe
     */
    boolean existsByParkingLotIdAndSpotNumber(Long parkingLotId, String spotNumber);

    /**
     * Trouver les places par étage
     */
    List<ParkingSpot> findByParkingLotIdAndFloorNumber(Long parkingLotId, Integer floorNumber);

    /**
     * Trouver les places par section
     */
    List<ParkingSpot> findByParkingLotIdAndSection(Long parkingLotId, String section);
}