// cl.ucm.bookapi.apibook.service.BookingService.java
package cl.ucm.bookapi.apibook.service;

import cl.ucm.bookapi.apibook.dto.BookingCopyBookDTO;
import cl.ucm.bookapi.apibook.dto.BookingRequest;
import cl.ucm.bookapi.apibook.dto.BookingResponse;
import cl.ucm.bookapi.apibook.entity.Booking;
import cl.ucm.bookapi.apibook.entity.CopyBook;
import cl.ucm.bookapi.apibook.entity.User;
import cl.ucm.bookapi.apibook.entity.Fine; // <-- Nueva importación
import cl.ucm.bookapi.apibook.repository.BookingRepository;
import cl.ucm.bookapi.apibook.repository.CopyBookRepository;
import cl.ucm.bookapi.apibook.repository.UserRepository;
import cl.ucm.bookapi.apibook.repository.FineRepository; // <-- Nueva importación
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <-- Importación para @Transactional
import java.math.BigDecimal; // <-- Nueva importación para multas
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit; // <-- Nueva importación para calcular días
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

    /**
     * Crea una nueva reserva para un usuario y una copia de libro específica.
     * @param request El DTO con el ID del usuario y el ID de la copia del libro.
     * @return La entidad Booking creada y guardada.
     * @throws EntityNotFoundException Si el usuario o la copia del libro no se encuentran.
     * @throws RuntimeException Si la copia del libro no está disponible o el usuario está bloqueado/multado.
     */
    @Transactional // Añadir @Transactional para asegurar que todas las operaciones son atómicas
    public Booking createBooking(BookingRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario con ID " + request.getUserId() + " no encontrado."));

        // Validar que el usuario no esté bloqueado/multado
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
        // Establecer la fecha de devolución esperada: 5 días después de la fecha de préstamo 
        booking.setExpectedReturnDate(LocalDateTime.now().plusDays(5));
        booking.setState(true); // Estado activo para una nueva reserva

        Booking savedBooking = bookingRepository.save(booking);

        copyBook.setState(false); // La copia ya no está disponible
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
     * Procesa la devolución de una reserva de libro, aplicando multas si es tardía
     * y actualizando el estado del lector si es multado.
     * @param idBooking El ID de la reserva a devolver.
     * @return La reserva actualizada después de la devolución.
     * @throws EntityNotFoundException Si la reserva no se encuentra.
     * @throws RuntimeException Si la reserva ya ha sido devuelta.
     */
    @Transactional // Añadir @Transactional para asegurar que todas las operaciones son atómicas
    public Booking returnBook(Long idBooking) {
        // 1. Buscar la reserva
        Booking booking = bookingRepository.findById(idBooking)
                .orElseThrow(() -> new EntityNotFoundException("Reserva con ID " + idBooking + " no encontrada."));

        // 2. Verificar si la reserva ya ha sido devuelta (state es false)
        if (booking.getState() == null || !booking.getState()) {
            throw new RuntimeException("La reserva con ID " + idBooking + " ya ha sido devuelta o está inactiva.");
        }

        // 3. Actualizar la reserva: fecha de devolución y estado
        LocalDateTime returnDate = LocalDateTime.now();
        booking.setDateReturn(returnDate); // Establece la fecha y hora actual de devolución
        booking.setState(false); // Cambia el estado de la reserva a inactiva/devuelta

        // 4. Calcular si hay multa y aplicarla [cite: 25, 90]
        LocalDateTime expectedReturnDate = booking.getExpectedReturnDate();
        long daysLate = ChronoUnit.DAYS.between(expectedReturnDate.toLocalDate(), returnDate.toLocalDate()); // Solo compara días

        if (daysLate > 0) { // Si hay días de retraso 
            User user = booking.getUser(); // El usuario asociado a la reserva
            BigDecimal fineAmount = BigDecimal.valueOf(daysLate * 1000); // $1000 por día 

            // Crear y guardar la multa 
            Fine fine = new Fine();
            fine.setUser(user);
            fine.setBooking(booking); // Asociar la multa a la reserva
            fine.setAmount(fineAmount);
            fine.setDescription("Multa por devolución tardía de: " + booking.getCopyBook().getBook().getTitle() + " (Copia: " + booking.getCopyBook().getUniqueCode() + "). Días de retraso: " + daysLate);
            fine.setFineDate(LocalDateTime.now());
            fine.setState("PENDIENTE"); // Estado inicial de la multa 
            fineRepository.save(fine);

            // Actualizar estado del lector a 'multado' (false) 
            if (user.getState() != null && user.getState()) { // Si no está ya inactivo
                user.setState(false);
                userRepository.save(user);
            }
        }

        // 5. Guardar la reserva actualizada
        Booking updatedBooking = bookingRepository.save(booking);

        // 6. Obtener la copia del libro asociada y cambiar su estado a disponible 
        CopyBook copyBook = updatedBooking.getCopyBook();
        if (copyBook == null) {
            throw new IllegalStateException("La reserva no tiene una copia de libro asociada, lo cual es inconsistente.");
        }

        copyBook.setState(true); // La copia vuelve a estar disponible 
        copyBookRepository.save(copyBook);

        return updatedBooking; // Devolver la reserva actualizada
    }
}