package argolo.tech.springsecurity6.controller;

import argolo.tech.springsecurity6.controller.dto.AppointmentDto;
import argolo.tech.springsecurity6.entities.Appointment;
import argolo.tech.springsecurity6.entities.User;
import argolo.tech.springsecurity6.repository.AppointmentRespository;
import argolo.tech.springsecurity6.repository.ProceduresRepositiry;
import argolo.tech.springsecurity6.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
    public ResponseEntity<Void> createAppointment(@RequestBody AppointmentDto appointmentDto, JwtAuthenticationToken token) {
        var proceduresOptional = proceduresRepositiry.findByName(appointmentDto.procedures().name());
        var procedures = proceduresOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var user = userRepository.findById(UUID.fromString(token.getName()));
        var num = appointmentRespository.count();
        var appointment = new Appointment();
        if (user.isPresent() && num <= 40) {
            appointment.setUser(user.get());
            appointment.setProcedures(procedures);
            appointment.setAppointmentDate(appointmentDto.appointmentDate());
            appointment.setHealthCenter(appointmentDto.healthCenter().toLowerCase());
            appointment.setStatus(appointmentDto.status().toLowerCase());
        } else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        appointmentRespository.save(appointment);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/checkifscheduled")
    public boolean isScheduled(@RequestBody User user) {
        return appointmentRespository.existsByUser(user);
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_admin')")
    @GetMapping("/AppointmentForToday")
    public ResponseEntity<List<Appointment>> getAppointmentByData() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyy");
        String todayData = today.format(formatter);
        var appointmentListOptional = appointmentRespository.findAppointmentByAppointmentDate(todayData);
        var appointment = appointmentListOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No appointment found!"));
        return ResponseEntity.ok(appointment);
    }

    @GetMapping("/userappointment")
    public ResponseEntity<Appointment> getUserAppointment(JwtAuthenticationToken token) {
        var appointment = getAppointmentByUserToken(token);
        if (!appointment.getStatus().equals("agendado")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No pending appointment");
        }
        appointment.setPosition(appointmentRespository.findPositionById(appointment.getId()));
        return ResponseEntity.ok(appointment);
    }

    @DeleteMapping("/deleteAppointment")
    public ResponseEntity<Void> deleteAppointment(JwtAuthenticationToken token) {
        var appointment = getAppointmentByUserToken(token);
        appointment.setProcedures(null);
        appointmentRespository.delete(appointment);
        return ResponseEntity.ok().build();
    }

    private Appointment getAppointmentByUserToken(JwtAuthenticationToken token) {
        var userOptional = userRepository.findById(UUID.fromString(token.getName()));
        userOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        var appointmentOptional = appointmentRespository.findAppointmentByUser(userOptional.get());
        return appointmentOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found"));
    }

}
