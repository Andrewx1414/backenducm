// cl.ucm.bookapi.apibook.repository.CopyBookRepository.java
package cl.ucm.bookapi.apibook.repository;

import cl.ucm.bookapi.apibook.entity.CopyBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // Importar List

@Repository
public interface CopyBookRepository extends JpaRepository<CopyBook, Long> {

    // --- ¡NUEVO MÉTODO PARA BUSCAR COPIAS DISPONIBLES POR TÍTULO DEL LIBRO! ---
    // 'findByBook' se refiere a la propiedad 'book' en CopyBook
    // 'TitleContainingIgnoreCase' busca el título del libro, ignorando mayúsculas/minúsculas
    // 'AndState' filtra por el estado de la copia (true = disponible)
    List<CopyBook> findByBookTitleContainingIgnoreCaseAndState(String title, Boolean state);
}