package com.example.parking_rmi.controller;

import com.example.parking_rmi.dto.ReservationDTO;
import com.example.parking_rmi.service.ParkingServ;
import com.example.parking_rmi.Interface.ParkingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc; // Outil pour simuler les requêtes

    @MockBean
    private ParkingServ clientService; // On mock le wrapper

    @MockBean
    private ParkingService parkingService; // On mock le service RMI

    @Autowired
    private ObjectMapper objectMapper; // Pour convertir Java -> JSON

    @Test
    public void testCreateReservation_Success() throws Exception {
        // Donnée fictive
        ReservationDTO dto = new ReservationDTO();
        dto.setParkingLotId(1L);
        dto.setParkingSpotId(10L);
        dto.setUserEmail("fatima@test.com");

        // Simulation : Le service retourne l'objet créé
        when(clientService.createReservation(any(ReservationDTO.class))).thenReturn(dto);

        // Test de l'URL POST
        mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated()); // On attend un code 201 Created
    }

    @Test
    public void testCancelReservation() throws Exception {
        Long id = 123L;
        when(clientService.cancelReservation(id)).thenReturn(true);

        // Test de l'URL POST /{id}
        mockMvc.perform(post("/api/reservations/" + id))
                .andExpect(status().isOk()); // On attend un code 200 
    }
}