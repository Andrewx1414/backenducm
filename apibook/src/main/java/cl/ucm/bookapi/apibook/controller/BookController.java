package cl.ucm.bookapi.apibook.controller;

import cl.ucm.bookapi.apibook.dto.NewBookRequest;
import cl.ucm.bookapi.apibook.dto.NewCopyBookRequest;
import cl.ucm.bookapi.apibook.entity.Book;
import cl.ucm.bookapi.apibook.entity.CopyBook;
import cl.ucm.bookapi.apibook.service.BookService;
import cl.ucm.bookapi.apibook.service.CopyBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // ¡Asegúrate de importar esta anotación!
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private CopyBookService copyBookService; // ¡Correctamente inyectado!

    // Endpoint público para obtener todos los libros
    @GetMapping("/all")
    public ResponseEntity<List<Book>> getAllBooks() { // Cambiado a ResponseEntity para mejor control
        List<Book> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    // Endpoint público para obtener libros por tipo (según tu lista)
    // Asumo que bookService.getBooksByType(type) ya existe
    @GetMapping("/all/{type}")
    public ResponseEntity<List<Book>> getBooksByType(@PathVariable String type) { // Cambiado a ResponseEntity
        List<Book> books = bookService.getBooksByType(type);
        if (books.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    // Endpoint ADMIN: Buscar libro por título
    @GetMapping("/find/{title}")
    @PreAuthorize("hasRole('ADMIN')") // ¡Sugerencia! Proteger este endpoint para ADMINs
    public ResponseEntity<List<Book>> getBooksByTitle(@PathVariable String title) { // Cambiado a ResponseEntity
        List<Book> books = bookService.getBooksByTitle(title); // Usar el método searchBooksByTitle que creamos
        if (books.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    // Endpoint ADMIN: Crear nuevo libro
    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')") // ¡Sugerencia! Proteger este endpoint para ADMINs
    public ResponseEntity<String> createBook(@RequestBody NewBookRequest request) {
        bookService.createBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("📘 Libro creado correctamente"); // 201 Created
    }

    // Endpoint ADMIN: Crear nueva copia de un libro
    @PostMapping("/newcopy")
    @PreAuthorize("hasRole('ADMIN')") // ¡Descomentado y activado! Proteger este endpoint para ADMINs
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

    // --- ¡NUEVO ENDPOINT! ADMIN: Buscar copias disponibles de un libro por título ---
    @GetMapping("/copy/{title}")
    @PreAuthorize("hasRole('ADMIN')") // Proteger este endpoint para ADMINs
    public ResponseEntity<List<CopyBook>> getAvailableCopiesByBookTitle(@PathVariable String title) {
        List<CopyBook> availableCopies = copyBookService.findAvailableCopiesByBookTitle(title);

        if (availableCopies.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 si no hay copias disponibles
        }
        return new ResponseEntity<>(availableCopies, HttpStatus.OK); // 200 OK con la lista de copias
    }
}