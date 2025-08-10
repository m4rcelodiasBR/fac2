package mb.cpo.facdigital.controller;

import lombok.RequiredArgsConstructor;
import mb.cpo.facdigital.dto.avaliacao.AvaliacaoResumoDTO;
import mb.cpo.facdigital.security.UsuarioPrincipal;
import mb.cpo.facdigital.service.AvaliacaoService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final AvaliacaoService avaliacaoService;

    /**
     * Este método é chamado quando o usuário acessa a URL /dashboard.
     * Ele só é acessível para usuários autenticados.
     * @param usuarioLogado O objeto do usuário autenticado, injetado pelo Spring Security.
     * @param model O objeto usado para passar dados do controller para a página HTML.
     * @return O nome do arquivo HTML a ser renderizado.
     */
    @GetMapping
    public String dashboard(@AuthenticationPrincipal UsuarioPrincipal usuarioLogado, Model model) {
        String nipAvaliador = usuarioLogado.getUsername();
        List<AvaliacaoResumoDTO> avaliacoes = avaliacaoService.listarAvaliacoesPorAvaliador(nipAvaliador);

        model.addAttribute("avaliacoes", avaliacoes);

        return "dashboard";
    }
}