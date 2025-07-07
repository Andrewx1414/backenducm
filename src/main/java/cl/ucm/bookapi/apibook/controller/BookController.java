package cl.ucm.bookapi.apibook.controller;

import cl.ucm.bookapi.apibook.dto.NewBookRequest;
import cl.ucm.bookapi.apibook.dto.NewCopyBookRequest;
import cl.ucm.bookapi.apibook.dto.BookingCopyBookDTO;
import cl.ucm.bookapi.apibook.dto.BookCopyCountDTO;
import cl.ucm.bookapi.apibook.entity.Book;
import cl.ucm.bookapi.apibook.entity.CopyBook;
import cl.ucm.bookapi.apibook.service.BookService;
import cl.ucm.bookapi.apibook.service.CopyBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private CopyBookService copyBookService;

    // Endpoint público para obtener todos los libros
    @GetMapping("/all")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    // Endpoint público para obtener libros por tipo
    @GetMapping("/all/{type}")
    public ResponseEntity<List<Book>> getBooksByType(@PathVariable String type) {
        List<Book> books = bookService.getBooksByType(type);
        if (books.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    // Endpoint ADMIN: Buscar libro por título
    @GetMapping("/find/{title}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<Book>> getBooksByTitle(@PathVariable String title) {
        List<Book> books = bookService.getBooksByTitle(title);
        if (books.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    // Endpoint ADMIN: Crear nuevo libro
    @PostMapping("/new")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> createBook(@RequestBody NewBookRequest request) {
        bookService.createBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Libro creado correctamente");
    }

    // Endpoint ADMIN: Crear nueva copia de un libro
    @PostMapping("/newcopy")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> createNewCopyBook(@RequestBody NewCopyBookRequest request) {
        try {
            CopyBook newCopy = copyBookService.createCopyBook(request.getBookId());
            return ResponseEntity.status(HttpStatus.CREATED).body("Nueva copia de libro creada con ID: " + newCopy.getIdCopyBook());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la copia del libro: " + e.getMessage());
        }
    }

    // ENDPOINT ADMIN: Buscar copias disponibles de un libro por título
    @GetMapping("/copy/{title}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<CopyBook>> getAvailableCopiesByBookTitle(@PathVariable("title") String title) {
        List<CopyBook> availableCopies = copyBookService.findAvailableCopiesByBookTitle(title);

        if (availableCopies.isEmpty()) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(availableCopies, HttpStatus.OK);
    }

    // Endpoint ADMIN: Obtener todas las copias de libros con detalles de sus libros
    @GetMapping("/copies/details")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<BookingCopyBookDTO>> getAllCopiesWithDetails() {
        List<BookingCopyBookDTO> copiesWithDetails = copyBookService.getAllCopiesWithBookDetails();
        if (copiesWithDetails.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(copiesWithDetails, HttpStatus.OK);
    }

    //ENDPOINT PUBLICO: Obtener el número de copias por cada libro
    @GetMapping("/copies/count")
    public ResponseEntity<List<BookCopyCountDTO>> getCopyCountsPerBook() {
        List<BookCopyCountDTO> copyCounts = copyBookService.getBookCopyCounts();
        if (copyCounts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(copyCounts, HttpStatus.OK);
    }
}