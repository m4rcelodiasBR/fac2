package mb.cpo.facdigital.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        // Este método simplesmente exibe a página de login.
        // O Spring Security cuidará do processo de autenticação (o POST) automaticamente.
        return "login";
    }
}