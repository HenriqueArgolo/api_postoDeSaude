package argolo.tech.springsecurity6.repository;

import argolo.tech.springsecurity6.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
    Role findByRoleId(Long id);
}
