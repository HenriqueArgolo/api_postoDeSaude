package argolo.tech.springsecurity6.controller.dto;

import argolo.tech.springsecurity6.entities.Procedures;

public record AppointmentDto(
        ProceduresDto procedures,
        String healthCenter,
        String appointmentDate,
        String status
) {

}
