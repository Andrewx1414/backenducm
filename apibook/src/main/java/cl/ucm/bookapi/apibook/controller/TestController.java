package cl.ucm.bookapi.apibook.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/protected")
    public String testProtected() {
        return "✅ Acceso autorizado al endpoint protegido.";
    }
}
