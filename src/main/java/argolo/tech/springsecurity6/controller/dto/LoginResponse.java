package argolo.tech.springsecurity6.controller.dto;

public record LoginResponse(String acessToken, Long expiresIn) {
}
