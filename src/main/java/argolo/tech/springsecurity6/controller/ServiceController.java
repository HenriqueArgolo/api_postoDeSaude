package argolo.tech.springsecurity6.controller;

import argolo.tech.springsecurity6.repository.AppointmentRespository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/startService")
public class ServiceController {
    final
    AppointmentRespository appointmentRespository;

    public ServiceController(AppointmentRespository appointmentRespository) {
        this.appointmentRespository = appointmentRespository;
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_admin')")
    private ResponseEntity<Void> startService(@PathVariable Long id) {
        var appointmentOptional = appointmentRespository.findAppointmentById(id);
        var appointment = appointmentOptional.orElseThrow(() -> new ResponseStatusException((HttpStatus.NOT_FOUND), "Not Found"));
        if(appointment.getStatus().equals("agendado")){
            appointment.setStatus("finalizado");
            appointmentRespository.save(appointment);
        } else return ResponseEntity.status(HttpStatus.CONFLICT).build();

        return ResponseEntity.ok().build();
    }
}
