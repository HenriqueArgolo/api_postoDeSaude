package argolo.tech.springsecurity6.repository;

import argolo.tech.springsecurity6.entities.Appointment;
import argolo.tech.springsecurity6.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppointmentRespository extends JpaRepository<Appointment, Long> {

    Optional<Appointment> findAppointmentByUser(User user);
    Optional<Appointment> findAppointmentById(Long id);
    Boolean existsByUser(User user);

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.id <= :appointmentId")
    Integer findPositionById(@Param("appointmentId") Long appointmentId);
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO tb_history (id, appointment_date, creation_times_tamp, health_center, procedures_id, status, user_id)  " +
            "SELECT appointment_id, appointment_date, creation_times_tamp, health_center, procedures_id, status, user_id " +
            "FROM tb_appointment WHERE appointment_id = :id", nativeQuery = true)
    void moveToHistory(@Param("id") Long id);


    @Transactional
    @Modifying
    @Query("DELETE FROM Appointment a WHERE a.id = :id ")
    void deleteById(@Param("id") Long id);
}