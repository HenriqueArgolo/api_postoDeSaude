package argolo.tech.springsecurity6.repository;

import argolo.tech.springsecurity6.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUserName(String userName);
    Optional<User>findUserByEmail(String email);
    Optional<User> findById(UUID id);
}
