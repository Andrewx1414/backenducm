package cl.ucm.bookapi.apibook.repository;

import cl.ucm.bookapi.apibook.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
