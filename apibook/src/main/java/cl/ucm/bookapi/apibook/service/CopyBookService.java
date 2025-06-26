// cl.ucm.bookapi.apibook.service.CopyBookService.java
package cl.ucm.bookapi.apibook.service;

import cl.ucm.bookapi.apibook.entity.Book;
import cl.ucm.bookapi.apibook.entity.CopyBook; // Importa tu nueva entidad
import cl.ucm.bookapi.apibook.repository.BookRepository; // Necesitarás este para buscar el Book
import cl.ucm.bookapi.apibook.repository.CopyBookRepository; // Importa tu nuevo repositorio
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException; // Para manejar el caso de libro no encontrado

@Service
public class CopyBookService {

    @Autowired
    private CopyBookRepository copyBookRepository;

    @Autowired
    private BookRepository bookRepository; // Para poder buscar el libro por ID

    public CopyBook createCopyBook(Long bookId) {
        // 1. Buscar el libro al que se asociará la copia
        Book book = bookRepository.findById(bookId)
                                  .orElseThrow(() -> new EntityNotFoundException("Libro con ID " + bookId + " no encontrado."));

        // 2. Crear una nueva instancia de CopyBook
        CopyBook copyBook = new CopyBook();
        copyBook.setBook(book);        // Asocia el libro encontrado
        copyBook.setState(true);          // Establece el estado inicial como "disponible" (1)

        // 3. Guardar la nueva copia en la base de datos
        return copyBookRepository.save(copyBook);
    }
}