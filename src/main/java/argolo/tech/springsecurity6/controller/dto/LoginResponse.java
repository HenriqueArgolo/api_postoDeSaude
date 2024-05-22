package argolo.tech.springsecurity6.controller.dto;

public record LoginResponse(String accessToken, Long expiresIn, String role) {
}
