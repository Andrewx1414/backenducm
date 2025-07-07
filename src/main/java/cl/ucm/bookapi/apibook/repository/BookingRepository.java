package cl.ucm.bookapi.apibook.repository;

import cl.ucm.bookapi.apibook.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserEmail(String email);
    List<Booking> findByUserEmailAndState(String email, Boolean state);


    @Query("SELECT b FROM Booking b " +
           "JOIN FETCH b.user u " +
           "JOIN FETCH b.copyBook cb " +
           "JOIN FETCH cb.book " +
           "WHERE u.email = :email")
    List<Booking> findBookingsWithDetailsByUserEmail(@Param("email") String email);
}