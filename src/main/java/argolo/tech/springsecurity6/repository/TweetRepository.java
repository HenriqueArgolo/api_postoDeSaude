package argolo.tech.springsecurity6.repository;

import argolo.tech.springsecurity6.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TweetRepository extends JpaRepository<Appointment, Long> {
    Optional<Appointment> findById(Long id);
}
