package argolo.tech.springsecurity6.controller.dto;

import argolo.tech.springsecurity6.entities.User;

import java.util.Optional;

public record LoginResponse(Optional<User> user, String accessToken, Long expiresIn, String role) {
}
