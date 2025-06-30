// cl.ucm.bookapi.apibook.repository.FineRepository.java
package cl.ucm.bookapi.apibook.repository;

import cl.ucm.bookapi.apibook.entity.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {

    // --- ¡QUERY PERSONALIZADA PARA BUSCAR POR EMAIL DE USUARIO! ---
    // Spring Data JPA puede crear automáticamente esta query si sigues la convención de nombres.
    // 'user.email' se refiere al campo 'email' dentro de la entidad 'User' asociada a 'Fine'.
    List<Fine> findByUserEmail(String email);
}