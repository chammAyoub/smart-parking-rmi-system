package com.example.parking_rmi.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.parking_rmi.model.ParkingLot;
import com.example.parking_rmi.model.ParkingLot.ParkingStatus;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour la gestion des parkings
 * 
 * @author Omar - Backend Core
 */
@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, Long> {

    
    Optional<ParkingLot> findByName(String name);

    /**
     * Trouver un parking par nom de service RMI
     */
    Optional<ParkingLot> findByRmiServiceName(String rmiServiceName);

    /**
     * Trouver tous les parkings par statut
     */
    List<ParkingLot> findByStatus(ParkingStatus status);

    /**
     * Trouver tous les parkings actifs
     */
    @Query("SELECT p FROM ParkingLot p WHERE p.status = 'ACTIVE'")
    List<ParkingLot> findAllActive();

    /**
     * Trouver tous les parkings avec places disponibles
     */
    @Query("SELECT p FROM ParkingLot p WHERE p.status = 'ACTIVE' AND p.availableSpots > 0")
    List<ParkingLot> findAllWithAvailableSpots();

    /**
     * Trouver les parkings par ville
     */
    List<ParkingLot> findByCity(String city);

    /**
     * Trouver les parkings dans un rayon donné (en km)
     */
    @Query("SELECT p FROM ParkingLot p WHERE p.status = 'ACTIVE' " +
           "AND (6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) * " +
           "cos(radians(p.longitude) - radians(:longitude)) + sin(radians(:latitude)) * " +
           "sin(radians(p.latitude)))) < :radius")
    List<ParkingLot> findNearby(@Param("latitude") Double latitude,
                                 @Param("longitude") Double longitude,
                                 @Param("radius") Double radius);

    /**
     * Compter les parkings par statut
     */
    Long countByStatus(ParkingStatus status);

    /**
     * Compter le nombre total de places disponibles
     */
    @Query("SELECT SUM(p.availableSpots) FROM ParkingLot p WHERE p.status = 'ACTIVE'")
    Integer getTotalAvailableSpots();

    /**
     * Compter le nombre total de places
     */
    @Query("SELECT SUM(p.totalSpots) FROM ParkingLot p WHERE p.status = 'ACTIVE'")
    Integer getTotalSpots();

    /**
     * Vérifier si un parking existe par nom
     */
    boolean existsByName(String name);

    /**
     * Vérifier si un parking existe par service RMI
     */
    boolean existsByRmiServiceName(String rmiServiceName);
}