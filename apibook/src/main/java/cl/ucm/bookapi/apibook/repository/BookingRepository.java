// cl.ucm.bookapi.apibook.repository.BookingRepository.java
package cl.ucm.bookapi.apibook.repository;

import cl.ucm.bookapi.apibook.entity.Booking; // Importa tu entidad Booking
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Importar @Query
import org.springframework.data.repository.query.Param; // Importar @Param
import org.springframework.stereotype.Repository;
import java.util.List; // Importa List
import java.util.Optional; // Importa Optional (aunque no lo usaremos directamente en el nuevo método, es bueno tenerlo)

@Repository // Anotación para indicar que es un componente de repositorio
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Mantengo los métodos originales, aunque el siguiente @Query es más robusto para la respuesta DTO
    List<Booking> findByUserEmail(String email);
    List<Booking> findByUserEmailAndState(String email, Boolean state);

    /**
     * Busca todas las reservas asociadas a un usuario dado su email,
     * cargando explícitamente el User, CopyBook y el Book asociado en la misma consulta
     * para evitar problemas de lazy loading (ByteBuddyInterceptor) y N+1 queries.
     *
     * @param email El email del usuario.
     * @return Una lista de reservas con todos los detalles cargados.
     */
    @Query("SELECT b FROM Booking b " +
           "JOIN FETCH b.user u " +
           "JOIN FETCH b.copyBook cb " +
           "JOIN FETCH cb.book " + // Asegúrate de que Book esté también mapeado en CopyBook con getBook()
           "WHERE u.email = :email")
    List<Booking> findBookingsWithDetailsByUserEmail(@Param("email") String email);
}