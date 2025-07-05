package cl.ucm.bookapi.apibook.repository;

import cl.ucm.bookapi.apibook.entity.CopyBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CopyBookRepository extends JpaRepository<CopyBook, Long> {

    List<CopyBook> findByBookTitleContainingIgnoreCaseAndState(String title, Boolean state);
}