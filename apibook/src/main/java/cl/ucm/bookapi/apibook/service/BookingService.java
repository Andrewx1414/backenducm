package cl.ucm.bookapi.apibook.service;

import cl.ucm.bookapi.apibook.dto.BookingCopyBookDTO;
import cl.ucm.bookapi.apibook.dto.BookingRequest;
import cl.ucm.bookapi.apibook.dto.BookingResponse;
import cl.ucm.bookapi.apibook.entity.Booking;
import cl.ucm.bookapi.apibook.entity.CopyBook;
import cl.ucm.bookapi.apibook.entity.User;
import cl.ucm.bookapi.apibook.entity.Fine;
import cl.ucm.bookapi.apibook.repository.BookingRepository;
import cl.ucm.bookapi.apibook.repository.CopyBookRepository;
import cl.ucm.bookapi.apibook.repository.UserRepository;
import cl.ucm.bookapi.apibook.repository.FineRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CopyBookRepository copyBookRepository;

    @Autowired
    private FineRepository fineRepository; 

    
     //Crea una nueva reserva para un usuario y una copia de libro específica.
    @Transactional
    public Booking createBooking(BookingRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario con ID " + request.getUserId() + " no encontrado."));

        if (user.getState() == null || !user.getState()) {
            throw new RuntimeException("El usuario con email " + user.getEmail() + " está bloqueado o multado y no puede realizar reservas.");
        }

        CopyBook copyBook = copyBookRepository.findById(request.getCopyBookId())
                .orElseThrow(() -> new EntityNotFoundException("Copia de libro con ID " + request.getCopyBookId() + " no encontrada."));

        if (copyBook.getState() == null || !copyBook.getState()) {
            throw new RuntimeException("La copia del libro con ID " + request.getCopyBookId() + " no está disponible para reserva.");
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setCopyBook(copyBook);
        booking.setDateBooking(LocalDateTime.now());
        booking.setExpectedReturnDate(LocalDateTime.now().plusDays(5));
        booking.setState(true);

        Booking savedBooking = bookingRepository.save(booking);

        copyBook.setState(false);
        copyBookRepository.save(copyBook);

        return savedBooking;
    }

    
    //Busca todas las reservas asociadas a un usuario dado su email
    public List<BookingResponse> getBookingsByUserEmail(String email) {
        List<Booking> bookings = bookingRepository.findBookingsWithDetailsByUserEmail(email);

        return bookings.stream()
                .map(booking -> {
                    CopyBook copyBook = booking.getCopyBook();

                    BookingCopyBookDTO copyBookDTO = new BookingCopyBookDTO(
                            copyBook.getIdCopyBook(),
                            copyBook.getBook().getTitle(),
                            copyBook.getBook().getAuthor(),
                            copyBook.getBook().getType()
                    );

                    return new BookingResponse(
                            booking.getId(),
                            booking.getDateBooking(),
                            booking.getDateReturn(),
                            booking.getState(),
                            booking.getUser().getId(),
                            copyBookDTO
                    );
                })
                .collect(Collectors.toList());
    }

    
     //Procesa la devolución de una reserva de libro, aplicando multas si la devolucion es tardía

    @Transactional
    public Booking returnBook(Long idBooking) {
        Booking booking = bookingRepository.findById(idBooking)
                .orElseThrow(() -> new EntityNotFoundException("Reserva con ID " + idBooking + " no encontrada."));
        if (booking.getState() == null || !booking.getState()) {
            throw new RuntimeException("La reserva con ID " + idBooking + " ya ha sido devuelta o está inactiva.");
        }
        LocalDateTime returnDate = LocalDateTime.now();
        booking.setDateReturn(returnDate);
        booking.setState(false);
        LocalDateTime expectedReturnDate = booking.getExpectedReturnDate();
        long daysLate = ChronoUnit.DAYS.between(expectedReturnDate.toLocalDate(), returnDate.toLocalDate());

        if (daysLate > 0) { 
            User user = booking.getUser();
            BigDecimal fineAmount = BigDecimal.valueOf(daysLate * 1000);

            Fine fine = new Fine();
            fine.setUser(user);
            fine.setBooking(booking);
            fine.setAmount(fineAmount);
            fine.setDescription("Multa por devolución tardía de: " + booking.getCopyBook().getBook().getTitle() + " (Copia: " + booking.getCopyBook().getUniqueCode() + "). Días de retraso: " + daysLate);
            fine.setFineDate(LocalDateTime.now());
            fine.setState("PENDIENTE");
            fineRepository.save(fine);

            if (user.getState() != null && user.getState()) {
                user.setState(false);
                userRepository.save(user);
            }
        }

        Booking updatedBooking = bookingRepository.save(booking);
        CopyBook copyBook = updatedBooking.getCopyBook();
        if (copyBook == null) {
            throw new IllegalStateException("La reserva no tiene una copia de libro asociada, lo cual es inconsistente.");
        }

        copyBook.setState(true);
        copyBookRepository.save(copyBook);

        return updatedBooking;
    }
}