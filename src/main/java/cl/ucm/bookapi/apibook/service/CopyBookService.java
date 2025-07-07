package cl.ucm.bookapi.apibook.service;

import cl.ucm.bookapi.apibook.entity.Book;
import cl.ucm.bookapi.apibook.entity.CopyBook;
import cl.ucm.bookapi.apibook.dto.BookingCopyBookDTO;
import cl.ucm.bookapi.apibook.dto.BookCopyCountDTO;
import cl.ucm.bookapi.apibook.repository.BookRepository;
import cl.ucm.bookapi.apibook.repository.CopyBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class CopyBookService {

    @Autowired
    private CopyBookRepository copyBookRepository;

    @Autowired
    private BookRepository bookRepository;

public CopyBook createCopyBook(Long bookId) {
    Book book = bookRepository.findById(bookId)
                              .orElseThrow(() -> new EntityNotFoundException("Libro con ID " + bookId + " no encontrado."));
    CopyBook copyBook = new CopyBook();
    copyBook.setBook(book);
    copyBook.setState(true);
    return copyBookRepository.save(copyBook);
}

    public List<CopyBook> findAvailableCopiesByBookTitle(String title) {
        return copyBookRepository.findByBookTitleContainingIgnoreCaseAndState(title, true);
    }

    public List<BookingCopyBookDTO> getAllCopiesWithBookDetails() {
        return copyBookRepository.findAllCopiesWithBookDetails();
    }

    public List<BookCopyCountDTO> getBookCopyCounts() {
        return copyBookRepository.countCopiesPerBook();
    }
}