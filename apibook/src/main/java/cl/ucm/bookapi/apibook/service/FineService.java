// cl.ucm.bookapi.apibook.service.FineService.java
package cl.ucm.bookapi.apibook.service;

import cl.ucm.bookapi.apibook.entity.Fine;
import cl.ucm.bookapi.apibook.repository.FineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FineService {

    @Autowired
    private FineRepository fineRepository;

    // --- ¡MÉTODO PARA BUSCAR MULTAS POR EMAIL! ---
    public List<Fine> getFinesByUserEmail(String email) {
        // Llama a la query personalizada definida en el repositorio
        return fineRepository.findByUserEmail(email);
    }
}