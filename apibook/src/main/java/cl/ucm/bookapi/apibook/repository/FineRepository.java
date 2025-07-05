package cl.ucm.bookapi.apibook.repository;

import cl.ucm.bookapi.apibook.entity.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {

    List<Fine> findByUserEmail(String email);
}