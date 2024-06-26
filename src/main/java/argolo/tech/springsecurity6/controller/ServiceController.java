package argolo.tech.springsecurity6.controller;
import argolo.tech.springsecurity6.controller.dto.ObservationDto;
import argolo.tech.springsecurity6.entities.Appointment;
import argolo.tech.springsecurity6.entities.Historic;
import argolo.tech.springsecurity6.repository.AppointmentRespository;
import argolo.tech.springsecurity6.repository.HistoricRepository;
import argolo.tech.springsecurity6.repository.UserRepository;
import org.springframework.boot.availability.ApplicationAvailabilityBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/service")
public class ServiceController {
    final
    AppointmentRespository appointmentRespository;
    final HistoricRepository historyRepository;
    final UserRepository userRepository;


    public ServiceController(AppointmentRespository appointmentRespository, HistoricRepository historyRepository, UserRepository userRepository, ApplicationAvailabilityBean applicationAvailability) {
        this.appointmentRespository = appointmentRespository;
        this.historyRepository = historyRepository;
        this.userRepository = userRepository;

    }

    @PostMapping("/{id}")
    private ResponseEntity<Void> finishService(@RequestBody ObservationDto observationDto,@PathVariable Long id) {
      var appointment =  appointmentRespository.findAppointmentById(id);
      if (appointment.isEmpty()) {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointmet no found");
      }

        creatHistory(observationDto, appointment);
        return ResponseEntity.ok().build();
    }

    private void creatHistory(ObservationDto observationDto, Optional<Appointment> appointmentOptional) {
        var appointment = appointmentOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointmet no found"));
        var historic = appointmentToHistoric(appointment);
        historic.setObservation(observationDto.observation());
        historyRepository.save(historic);
        appointmentRespository.deleteById(appointment.getId());
    }

    private Historic appointmentToHistoric(Appointment appointment) {
        var historic = new Historic();
        historic.setId(appointment.getId());
        historic.setUser(appointment.getUser());
        historic.setHealthCenter(appointment.getHealthCenter());
        historic.setProcedures(appointment.getProcedures());
        historic.setStatus("Finalizado");
        historic.setAppointmentDate(appointment.getAppointmentDate());
        return historic;
    }



}

