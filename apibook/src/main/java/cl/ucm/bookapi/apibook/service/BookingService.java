// cl.ucm.bookapi.apibook.service.BookingService.java
package cl.ucm.bookapi.apibook.service;

import cl.ucm.bookapi.apibook.dto.BookingCopyBookDTO;
import cl.ucm.bookapi.apibook.dto.BookingRequest;
import cl.ucm.bookapi.apibook.dto.BookingResponse;
import cl.ucm.bookapi.apibook.entity.Booking;
import cl.ucm.bookapi.apibook.entity.CopyBook;
import cl.ucm.bookapi.apibook.entity.User;
import cl.ucm.bookapi.apibook.repository.BookingRepository;
import cl.ucm.bookapi.apibook.repository.CopyBookRepository;
import cl.ucm.bookapi.apibook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
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

    /**
     * Crea una nueva reserva para un usuario y una copia de libro específica.
     * @param request El DTO con el ID del usuario y el ID de la copia del libro.
     * @return La entidad Booking creada y guardada.
     * @throws EntityNotFoundException Si el usuario o la copia del libro no se encuentran.
     * @throws RuntimeException Si la copia del libro no está disponible.
     */
    public Booking createBooking(BookingRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario con ID " + request.getUserId() + " no encontrado."));

        CopyBook copyBook = copyBookRepository.findById(request.getCopyBookId())
                .orElseThrow(() -> new EntityNotFoundException("Copia de libro con ID " + request.getCopyBookId() + " no encontrada."));

        if (copyBook.getState() == null || !copyBook.getState()) {
            throw new RuntimeException("La copia del libro con ID " + request.getCopyBookId() + " no está disponible para reserva.");
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setCopyBook(copyBook);
        booking.setDateBooking(LocalDateTime.now());
        booking.setState(true);

        Booking savedBooking = bookingRepository.save(booking);

        copyBook.setState(false);
        copyBookRepository.save(copyBook);

        return savedBooking;
    }

    /**
     * Busca todas las reservas asociadas a un usuario dado su email y las mapea a DTOs de respuesta.
     * Utiliza una consulta con FETCH JOIN para cargar detalles de User, CopyBook y Book.
     * @param email El email del usuario.
     * @return Una lista de BookingResponse DTOs.
     */
    public List<BookingResponse> getBookingsByUserEmail(String email) {
        // *** IMPORTANTE: Asegúrate de que este método exista en BookingRepository con el FETCH JOIN ***
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

    /**
     * Procesa la devolución de una reserva de libro.
     * @param idBooking El ID de la reserva a devolver.
     * @return La reserva actualizada después de la devolución.
     * @throws EntityNotFoundException Si la reserva no se encuentra.
     * @throws RuntimeException Si la reserva ya ha sido devuelta.
     */
    public Booking returnBook(Long idBooking) {
        // 1. Buscar la reserva
        Booking booking = bookingRepository.findById(idBooking)
                .orElseThrow(() -> new EntityNotFoundException("Reserva con ID " + idBooking + " no encontrada."));

        // 2. Verificar si la reserva ya ha sido devuelta (state es false)
        if (booking.getState() == null || !booking.getState()) { // Si el estado es null o false
            throw new RuntimeException("La reserva con ID " + idBooking + " ya ha sido devuelta o está inactiva.");
        }

        // 3. Actualizar la reserva: fecha de devolución y estado
        booking.setDateReturn(LocalDateTime.now()); // Establece la fecha y hora actual de devolución
        booking.setState(false); // Cambia el estado de la reserva a inactiva/devuelta

        // 4. Guardar la reserva actualizada
        Booking updatedBooking = bookingRepository.save(booking);

        // 5. Obtener la copia del libro asociada y cambiar su estado a disponible
        CopyBook copyBook = updatedBooking.getCopyBook(); // La CopyBook debe estar cargada (o manejarse LazyInitializationException)
                                                          // En este contexto, al recuperar una Booking por ID, sus ManyToOne suelen ser inicializados.
        if (copyBook == null) {
            throw new IllegalStateException("La reserva no tiene una copia de libro asociada, lo cual es inconsistente.");
        }

        copyBook.setState(true); // La copia vuelve a estar disponible
        copyBookRepository.save(copyBook); // Guardar el cambio de estado de la copia

        return updatedBooking; // Devolver la reserva actualizada
    }
}