package argolo.tech.springsecurity6.controller;

import argolo.tech.springsecurity6.entities.Appointment;
import argolo.tech.springsecurity6.entities.History;
import argolo.tech.springsecurity6.repository.AppointmentRespository;
import argolo.tech.springsecurity6.repository.HistoryRepository;
import argolo.tech.springsecurity6.repository.UserRepository;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/service")
public class ServiceController {
    final
    AppointmentRespository appointmentRespository;
    final HistoryRepository historyRepository;
    final UserRepository userRepository;

    public ServiceController(AppointmentRespository appointmentRespository, HistoryRepository historyRepository, UserRepository userRepository) {
        this.appointmentRespository = appointmentRespository;
        this.historyRepository = historyRepository;
        this.userRepository = userRepository;
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> finishService(@PathVariable Long id, JwtAuthenticationToken token) {
        var appointmentOptional = appointmentRespository.findAppointmentById(id);
        var appointment = appointmentOptional.orElseThrow(() -> new ResponseStatusException((HttpStatus.NOT_FOUND), "Not Found"));

        if (appointment.getStatus().equals("agendado")) {
            appointment.setStatus("finalizado");
            setUserHistory(token, appointment);
            appointmentRespository.delete(appointment);
        } else return ResponseEntity.status(HttpStatus.CONFLICT).build();

        return ResponseEntity.ok().build();
    }


    private void setUserHistory(JwtAuthenticationToken token, Appointment appointment) {
        var userOptional = userRepository.findById(UUID.fromString(token.getName()));
        var user = userOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        user.getHistory().add(appointment);
        userRepository.save(user);
    }

}

