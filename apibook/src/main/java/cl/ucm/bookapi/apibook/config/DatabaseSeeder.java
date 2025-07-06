package cl.ucm.bookapi.apibook.config;

import cl.ucm.bookapi.apibook.entity.Role;
import cl.ucm.bookapi.apibook.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseSeeder {

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.findByName("ADMIN").isEmpty()) {
                roleRepository.save(new Role("ADMIN"));
            }
            if (roleRepository.findByName("LECTOR").isEmpty()) {
                roleRepository.save(new Role("LECTOR"));
            }
        };
    }
}
