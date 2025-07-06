
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

    public List<Fine> getFinesByUserEmail(String email) {
        return fineRepository.findByUserEmail(email);
    }
}