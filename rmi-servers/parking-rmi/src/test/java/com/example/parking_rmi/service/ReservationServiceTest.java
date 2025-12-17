package com.example.parking_rmi.service;

import com.example.parking_rmi.Repository.ReservationRepository;
import com.example.parking_rmi.Repository.ParkingSpotRepository;
import com.example.parking_rmi.dto.ReservationDTO;
import com.example.parking_rmi.model.Reservation;
import com.example.parking_rmi.model.ParkingSpot;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ParkingSpotRepository parkingSpotRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void testCancelReservation_Success() {
        // 1. Préparer les données fictives
        Long resId = 1L;
        Reservation mockRes = new Reservation();
        mockRes.setId(resId);
        mockRes.setStatus(Reservation.ReservationStatus.CONFIRMED);
        
        ParkingSpot mockSpot = new ParkingSpot();
        mockRes.setParkingSpot(mockSpot);

        // Dire à Mockito : "Si on cherche l'ID 1, retourne cette réservation"
        when(reservationRepository.findById(resId)).thenReturn(Optional.of(mockRes));
        
        // 2. Exécuter la méthode
        boolean result = reservationService.cancelReservation(resId);

        // 3. Vérifier le résultat
        assertTrue(result); // Ça doit retourner true
        verify(reservationRepository, times(1)).save(any(Reservation.class)); // On doit avoir sauvegardé
    }

    @Test
    void testGetReservationById_NotFound() {
        // Dire à Mockito : "Si on cherche l'ID 99, retourne vide"
        when(reservationRepository.findById(99L)).thenReturn(Optional.empty());

        ReservationDTO result = reservationService.getReservationById(99L);

        assertNull(result);
    }
}