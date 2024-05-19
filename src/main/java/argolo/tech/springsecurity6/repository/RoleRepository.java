package argolo.tech.springsecurity6.repository;

import argolo.tech.springsecurity6.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRoleName(String name);
}
