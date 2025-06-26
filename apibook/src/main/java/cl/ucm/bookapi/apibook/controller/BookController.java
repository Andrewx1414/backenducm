package cl.ucm.bookapi.apibook.controller;

import cl.ucm.bookapi.apibook.dto.NewBookRequest;
import cl.ucm.bookapi.apibook.dto.NewCopyBookRequest; // <-- ¡Asegúrate de que esta importación esté!
import cl.ucm.bookapi.apibook.entity.Book;
import cl.ucm.bookapi.apibook.entity.CopyBook; // <-- ¡Asegúrate de que esta importación esté!
import cl.ucm.bookapi.apibook.service.BookService;
import cl.ucm.bookapi.apibook.service.CopyBookService; // <-- ¡Asegúrate de que esta importación esté!
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.persistence.EntityNotFoundException; // <-- ¡Asegúrate de que esta importación esté!

@RestController
@RequestMapping("/api/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired // <-- ¡ESTA ES LA LÍNEA QUE TE FALTABA AÑADIR PARA INYECTARLO!
    private CopyBookService copyBookService;

    @GetMapping("/all")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/all/{type}")
    public List<Book> getBooksByType(@PathVariable String type) {
        return bookService.getBooksByType(type);
    }

    @GetMapping("/find/{title}")
    public List<Book> getBooksByTitle(@PathVariable String title) {
        return bookService.getBooksByTitle(title);
    }

    @PostMapping("/new")
    public ResponseEntity<String> createBook(@RequestBody NewBookRequest request) {
        bookService.createBook(request);
        return ResponseEntity.ok("📘 Libro creado correctamente");
    }

    // Endpoint para crear una nueva copia de un libro (PROTEGIDO - ROL ADMIN)
    @PostMapping("/newcopy")
    // @PreAuthorize("hasRole('ADMIN')") // Descomentar si usas seguridad a nivel de método
    public ResponseEntity<?> createNewCopyBook(@RequestBody NewCopyBookRequest request) {
        try {
            CopyBook newCopy = copyBookService.createCopyBook(request.getBookId());
            return ResponseEntity.status(HttpStatus.CREATED).body("📚 Nueva copia de libro creada con ID: " + newCopy.getIdCopyBook());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la copia del libro: " + e.getMessage());
        }
    }
}