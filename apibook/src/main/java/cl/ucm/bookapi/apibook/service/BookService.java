package cl.ucm.bookapi.apibook.service;

import cl.ucm.bookapi.apibook.dto.NewBookRequest;
import cl.ucm.bookapi.apibook.entity.Book;
import cl.ucm.bookapi.apibook.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;
    
    // Método para obtener todos los libros
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Método para obtener libros por tipo
    public List<Book> getBooksByType(String type) {
        return bookRepository.findByType(type); 
    }

    // Nuevo método para buscar libros por título (que contenga el texto e ignore mayúsculas/minúsculas)
    public List<Book> getBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    // Método para crear un nuevo libro
    public void createBook(NewBookRequest request) {
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setType(request.getType());

        if (request.getImage64() != null && !request.getImage64().isEmpty()) {
            String base64Image = request.getImage64();
            if (base64Image.contains(",")) {
                base64Image = base64Image.substring(base64Image.indexOf(",") + 1);
            }
            book.setImage64(base64Image);
        } else {
            book.setImage64(null);
        }

        bookRepository.save(book);
    }
}