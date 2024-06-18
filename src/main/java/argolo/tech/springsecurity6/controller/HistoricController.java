package argolo.tech.springsecurity6.controller;

import argolo.tech.springsecurity6.entities.Historic;
import argolo.tech.springsecurity6.repository.HistoricRepository;
import argolo.tech.springsecurity6.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/historic")
public class HistoricController {

    HistoricRepository historyRepository;
    UserRepository userRepository;

    public HistoricController(HistoricRepository historyRepository, UserRepository userRepository) {
        this.historyRepository = historyRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/all")
    private ResponseEntity<List<Historic>> getHistoric(JwtAuthenticationToken toke) {
        var userOptional = userRepository.findById(UUID.fromString(toke.getName()));
        var user = userOptional.orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(historyRepository.findAllByUser(user));
    }
}
