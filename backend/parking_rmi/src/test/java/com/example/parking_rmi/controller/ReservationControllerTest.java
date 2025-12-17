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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ReservationController.class)
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ParkingServ clientService;

    @MockBean
    private ParkingService parkingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateReservation_Success() throws Exception {
        // Donnée fictive
        ReservationDTO dto = new ReservationDTO();
        dto.setParkingLotId(1L);
        dto.setParkingSpotId(10L);
        dto.setUserEmail("fatima@test.com");

        // Simulation du service
        when(clientService.createReservation(any(ReservationDTO.class)))
                .thenReturn(dto);

        // Appel POST
        mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCancelReservation() throws Exception {
        Long id = 123L;

        when(clientService.cancelReservation(id)).thenReturn(true);

        mockMvc.perform(post("/api/reservations/" + id))
                .andExpect(status().isOk());
    }
}
