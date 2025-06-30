// cl.ucm.bookapi.apibook.service.CopyBookService.java
package cl.ucm.bookapi.apibook.service;

import cl.ucm.bookapi.apibook.entity.Book;
import cl.ucm.bookapi.apibook.entity.CopyBook;
import cl.ucm.bookapi.apibook.repository.BookRepository;
import cl.ucm.bookapi.apibook.repository.CopyBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.util.List; // Importar List

@Service
public class CopyBookService {

    @Autowired
    private CopyBookRepository copyBookRepository;

    @Autowired
    private BookRepository bookRepository;

    public CopyBook createCopyBook(Long bookId) {
        // 1. Buscar el libro al que se asociará la copia
        Book book = bookRepository.findById(bookId)
                                  .orElseThrow(() -> new EntityNotFoundException("Libro con ID " + bookId + " no encontrado."));

        // 2. Crear una nueva instancia de CopyBook
        CopyBook copyBook = new CopyBook();
        copyBook.setBook(book);
        copyBook.setState(true); // Establece el estado inicial como "disponible"

        // 3. Guardar la nueva copia en la base de datos
        return copyBookRepository.save(copyBook);
    }

    // --- ¡NUEVO MÉTODO PARA BUSCAR COPIAS DISPONIBLES POR TÍTULO DE LIBRO! ---
    public List<CopyBook> findAvailableCopiesByBookTitle(String title) {
        // Llama al método del repositorio para buscar copias que:
        // 1. Pertenecen a un libro cuyo título contenga la cadena (ignorando mayúsculas/minúsculas)
        // 2. Tengan el estado 'true' (disponible)
        return copyBookRepository.findByBookTitleContainingIgnoreCaseAndState(title, true);
    }
}