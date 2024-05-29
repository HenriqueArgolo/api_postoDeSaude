package argolo.tech.springsecurity6.controller;

import argolo.tech.springsecurity6.controller.dto.AppointmentDto;
import argolo.tech.springsecurity6.entities.Appointment;
import argolo.tech.springsecurity6.repository.AppointmentRespository;
import argolo.tech.springsecurity6.repository.ProceduresRepositiry;
import argolo.tech.springsecurity6.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class AppointmentController {


    final
    AppointmentRespository appointmentRespository;

    final
    UserRepository userRepository;

    final
    ProceduresRepositiry proceduresRepositiry;

    public AppointmentController(AppointmentRespository appointmentRespository, UserRepository userRepository, ProceduresRepositiry proceduresRepositiry) {
        this.appointmentRespository = appointmentRespository;
        this.userRepository = userRepository;
        this.proceduresRepositiry = proceduresRepositiry;
    }

    @PostMapping("/appointment")
    private ResponseEntity<Void> createAppointment(@RequestBody AppointmentDto appointmentDto, JwtAuthenticationToken token) {
        var procedures = proceduresRepositiry.findByName(appointmentDto.procedures().name());
        var user = userRepository.findById(UUID.fromString(token.getName()));
        var appointment = new Appointment();
        if (user.isPresent() && procedures.isPresent()) {
            appointment.setUser(user.get());
            appointment.setProcedures(procedures.get().getName());
            appointment.setAppointmentDate(appointmentDto.appointmentDate());
            appointment.setHealthCenter(appointmentDto.healthCenter().toLowerCase());
            appointment.setStatus(appointmentDto.status().toLowerCase());
        } else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        appointmentRespository.save(appointment);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/userappointment")
    private ResponseEntity<Appointment> getUserAppointment(JwtAuthenticationToken token) {
        var userOptional = userRepository.findById(UUID.fromString(token.getName()));
        var user = userOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        var AppointmentOpitional = appointmentRespository.findAppointmentByUser(user);
        var appointment = AppointmentOpitional.orElseThrow(() -> new ResponseStatusException((HttpStatus.NOT_FOUND), "Appointment not found"));
        if (!appointment.getStatus().equals("agendado")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No pending appointment");
        }
        return ResponseEntity.ok(appointment);

    }

}
