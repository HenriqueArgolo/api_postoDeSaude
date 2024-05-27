package argolo.tech.springsecurity6.controller;

import argolo.tech.springsecurity6.controller.dto.UserDto;
import argolo.tech.springsecurity6.entities.Role;
import argolo.tech.springsecurity6.entities.User;
import argolo.tech.springsecurity6.repository.RoleRepository;
import argolo.tech.springsecurity6.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test newUser when user already exists")
    void newUser() {
        // Given
        UserDto userDto = new UserDto("existingUser", "password");
        when(userRepository.findByUserName(userDto.username())).thenReturn(Optional.of(new User()));

        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userController.newUser(userDto);
        });

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("User already exists", exception.getReason());
    }

    @Test
    @DisplayName("Test newUser with valid user")
    void newUser_ValidUser() {
        // Given
        UserDto userDto = new UserDto("newUser", "password");
        Role userRole = new Role();
        userRole.setName(Role.Values.BASIC.name());

        when(userRepository.findByUserName(userDto.username())).thenReturn(Optional.empty());
        when(roleRepository.findByName(Role.Values.BASIC.name())).thenReturn(userRole);
        when(bCryptPasswordEncoder.encode(userDto.password())).thenReturn("encodedPassword");

        // When
        ResponseEntity<Object> response = userController.newUser(userDto);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Test listUsers returns list of users")
    void listUsers() {
        // Given
        User user = new User();
        user.setUserName("user1");
        when(userRepository.findAll()).thenReturn(List.of(user));

        // When
        ResponseEntity<List<User>> response = userController.listUsers();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("user1", response.getBody().get(0).getUserName());
    }
}
