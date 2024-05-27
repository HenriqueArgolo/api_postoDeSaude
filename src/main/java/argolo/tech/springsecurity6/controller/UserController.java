package argolo.tech.springsecurity6.controller;

import argolo.tech.springsecurity6.config.AdminUserConfig;
import argolo.tech.springsecurity6.controller.dto.UserDto;
import argolo.tech.springsecurity6.entities.Role;
import argolo.tech.springsecurity6.entities.User;
import argolo.tech.springsecurity6.repository.RoleRepository;
import argolo.tech.springsecurity6.repository.TweetRepository;
import argolo.tech.springsecurity6.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
public class UserController {
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  RoleRepository roleRepository;
    @Autowired
    private  BCryptPasswordEncoder bCryptPasswordEncoder;




    @Transactional
    @PostMapping("/users")
    public ResponseEntity<Object> newUser(@RequestBody UserDto userDto) {

        var userRole = roleRepository.findByName(Role.Values.BASIC.name());
        var userExist = userRepository.findByUserName(userDto.username());
        if(userExist.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }

        var user = new User();
        user.setUserName(userDto.username());
        user.setPassword(bCryptPasswordEncoder.encode(userDto.password()));
        user.setRoles(Set.of(userRole));

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
