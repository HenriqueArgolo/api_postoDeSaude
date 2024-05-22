package argolo.tech.springsecurity6.config;

import argolo.tech.springsecurity6.entities.Role;
import argolo.tech.springsecurity6.entities.User;
import argolo.tech.springsecurity6.repository.RoleRepository;
import argolo.tech.springsecurity6.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder PasswordEncoder;

    public AdminUserConfig(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        PasswordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        var role = roleRepository.findByName(Role.Values.BASIC.name());
        var userAdmin = userRepository.findByUserName("admin");

        userAdmin.ifPresentOrElse(
                (user) -> {
                    System.out.println("Admin já existe");
                },

                () -> {
                    var user = new User();
                    user.setUserName("admin1");
                    user.setPassword(PasswordEncoder.encode("123"));
                    user.setRoles(Set.of(role));
                    userRepository.save(user);
                }
        );
    }
}
