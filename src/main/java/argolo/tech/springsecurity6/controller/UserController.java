package argolo.tech.springsecurity6.controller;
import argolo.tech.springsecurity6.controller.dto.UserDto;
import argolo.tech.springsecurity6.entities.Appointment;
import argolo.tech.springsecurity6.entities.Role;
import argolo.tech.springsecurity6.entities.User;
import argolo.tech.springsecurity6.repository.AppointmentRespository;
import argolo.tech.springsecurity6.repository.RoleRepository;
import argolo.tech.springsecurity6.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
public class UserController {
        final
      UserRepository userRepository;
        final
      RoleRepository roleRepository;
        final
      BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AppointmentRespository appointmentRespository;

    public UserController(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AppointmentRespository appointmentRespository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.appointmentRespository = appointmentRespository;
    }


    @Transactional
    @PostMapping("/users")
    public ResponseEntity<Object> newUser(@RequestBody UserDto userDto) {

        var userRole = roleRepository.findByName(Role.Values.BASIC.name());
        var userExist = userRepository.findByUserName(userDto.userName());
        if(userExist.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }
        var user = new User();
        user.setFirstName(userDto.firstName());
        user.setLastName(userDto.lastName());
        user.setPassword(bCryptPasswordEncoder.encode(userDto.password()));
        user.setRoles(Set.of(userRole));
        user.setCpf(userDto.cpf());
        user.setSus(userDto.sus());
        user.setUserName(userDto.userName());
        user.setEmail(userDto.email());
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<List<User>> listUsers() {
        var users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }


}
