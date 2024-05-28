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
    void newUser_UserAlreadyExists_ShouldThrowException() {
        // Given
        UserDto userDto = new UserDto("John", "Doe", "12345678900", "98765432100", "johndoe", "john.doe@example.com", "password123");
        when(userRepository.findByUserName(userDto.userName())).thenReturn(Optional.of(new User()));

        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userController.newUser(userDto);
        });

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("User already exists", exception.getReason());
    }

    @Test
    @DisplayName("Test newUser when user does not exist")
    void newUser_UserDoesNotExist_ShouldCreateUser() {
        // Given
        UserDto userDto = new UserDto("Jane", "Doe", "22345678900", "88765432100", "janedoe", "jane.doe@example.com", "password123");
        when(userRepository.findByUserName(userDto.userName())).thenReturn(Optional.empty());
        Role userRole = new Role();
        when(roleRepository.findByName(Role.Values.BASIC.name())).thenReturn(userRole);
        when(bCryptPasswordEncoder.encode(userDto.password())).thenReturn("encodedPassword");

        // When
        ResponseEntity<Object> response = userController.newUser(userDto);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userRepository, times(1)).save(any(User.class));
    }
    @Test
    @DisplayName("Test listUsers when there are users")
    void listUsers_ShouldReturnListOfUsers() {
        // Given
        User user1 = new User();
        user1.setUserName("user1");
        User user2 = new User();
        user2.setUserName("user2");
        List<User> users = List.of(user1, user2);
        when(userRepository.findAll()).thenReturn(users);

        // When
        ResponseEntity<List<User>> response = userController.listUsers();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("user1", response.getBody().get(0).getUserName());
        assertEquals("user2", response.getBody().get(1).getUserName());
    }
}