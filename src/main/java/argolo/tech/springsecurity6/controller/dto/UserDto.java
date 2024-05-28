package argolo.tech.springsecurity6.controller.dto;

public record UserDto(
        String firstName,
        String lastName,
        String cpf,
        String sus,
        String userName,
        String email,
        String password
) {}