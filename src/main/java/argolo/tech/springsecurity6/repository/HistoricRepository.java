package argolo.tech.springsecurity6.repository;

import argolo.tech.springsecurity6.entities.Historic;
import argolo.tech.springsecurity6.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricRepository extends JpaRepository<Historic, Long> {

    List<Historic> findAllByUser(User user);
}
