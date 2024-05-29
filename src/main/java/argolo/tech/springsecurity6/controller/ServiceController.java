package argolo.tech.springsecurity6.controller;

import argolo.tech.springsecurity6.entities.Appointment;
import argolo.tech.springsecurity6.entities.History;
import argolo.tech.springsecurity6.repository.AppointmentRespository;
import argolo.tech.springsecurity6.repository.HistoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/service")
public class ServiceController {
    final
    AppointmentRespository appointmentRespository;
    final HistoryRepository historyRepository;

    public ServiceController(AppointmentRespository appointmentRespository, HistoryRepository historyRepository) {
        this.appointmentRespository = appointmentRespository;
        this.historyRepository = historyRepository;
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> serviceDone(@PathVariable Long id) {
        var history = new History();
        var appointmentOptional = appointmentRespository.findAppointmentById(id);
        var appointment = appointmentOptional.orElseThrow(() -> new ResponseStatusException((HttpStatus.NOT_FOUND), "Not Found"));

        if(appointment.getStatus().equals("agendado")){
            appointment.setStatus("finalizado");
            historyRepository.save(history);
            appointmentRespository.delete(appointment);
        } else return ResponseEntity.status(HttpStatus.CONFLICT).build();

        return ResponseEntity.ok().build();
    }

}

