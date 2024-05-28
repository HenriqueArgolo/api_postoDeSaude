package argolo.tech.springsecurity6.repository;

import argolo.tech.springsecurity6.entities.Procedures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProceduresRepositiry extends JpaRepository<Procedures, Long> {
    Optional<Procedures> findByName(java.lang.String name);
}
