package cl.ucm.bookapi.apibook.repository;

import cl.ucm.bookapi.apibook.entity.CopyBook;
import cl.ucm.bookapi.apibook.dto.BookingCopyBookDTO;
import cl.ucm.bookapi.apibook.dto.BookCopyCountDTO; // ¡Añadir este import!
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CopyBookRepository extends JpaRepository<CopyBook, Long> {

    List<CopyBook> findByBookTitleContainingIgnoreCaseAndState(String title, Boolean state);

    // Método existente para obtener detalles de copias de libros usando BookingCopyBookDTO
    @Query("SELECT new cl.ucm.bookapi.apibook.dto.BookingCopyBookDTO(cb.idCopyBook, cb.book.title, cb.book.author, cb.book.type) " +
           "FROM CopyBook cb")
    List<BookingCopyBookDTO> findAllCopiesWithBookDetails();

    // NUEVO método para obtener el conteo de copias por libro
    @Query("SELECT new cl.ucm.bookapi.apibook.dto.BookCopyCountDTO(cb.book.title, COUNT(cb.idCopyBook)) " +
           "FROM CopyBook cb GROUP BY cb.book.title")
    List<BookCopyCountDTO> countCopiesPerBook(); // <-- ¡Añadir este método!
}