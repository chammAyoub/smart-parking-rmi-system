package com.example.parking_rmi.Repository;

import com.example.parking_rmi.model.Reservation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.example.parking_rmi.Repository.ParkingLotRepository;
import com.example.parking_rmi.Repository.ParkingSpotRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    // On mock les autres repository pour ne pas avoir d'erreurs de dépendances
    @MockBean
    private ParkingLotRepository parkingLotRepository;
    @MockBean
    private ParkingSpotRepository parkingSpotRepository;

    @Test
    public void testFindReservationByEmail() {
        // Scénario : On cherche si la méthode renvoie quelque chose (même vide pour le test)
        String email = "test.fatima@gmail.com";
        
        // Action
        List<Reservation> result = reservationRepository.findByUserEmail(email);

        // Vérification (Assertion)
        assertThat(result).isNotNull(); // On vérifie juste que ça ne crash pas
    }

    @Test
    public void testFindActiveReservations() {
        // Action
        List<Reservation> active = reservationRepository.findActiveReservationsByUserEmail("test@gmail.com");
        
        // Vérification
        assertThat(active).isNotNull();
    }
}
