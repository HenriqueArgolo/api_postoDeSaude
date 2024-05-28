package argolo.tech.springsecurity6.repository;

import argolo.tech.springsecurity6.entities.Appointment;
import argolo.tech.springsecurity6.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AppointmentRespository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAppointmentByUser(User user);
}
