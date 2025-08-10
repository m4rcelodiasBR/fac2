package mb.cpo.facdigital.controller;

import lombok.RequiredArgsConstructor;
import mb.cpo.facdigital.dto.avaliacao.AvaliacaoDetalheDTO;
import mb.cpo.facdigital.dto.avaliacao.AvaliacaoResumoDTO;
import mb.cpo.facdigital.security.UsuarioPrincipal;
import mb.cpo.facdigital.service.AvaliacaoService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/fac")
@RequiredArgsConstructor
public class FacController {

    private final AvaliacaoService avaliacaoService;

    @GetMapping("/painel")
    public String painelDoAvaliador(@AuthenticationPrincipal UsuarioPrincipal usuarioLogado, Model model) {
        String nipAvaliador = usuarioLogado.getUsername();
        List<AvaliacaoResumoDTO> avaliacoes = avaliacaoService.listarAvaliacoesPorAvaliador(nipAvaliador);
        model.addAttribute("avaliacoes", avaliacoes);
        return "fac/painel";
    }

    @PostMapping("/carregar-xml")
    public String carregarXml(@RequestParam("arquivo") MultipartFile arquivo,
                              @AuthenticationPrincipal UsuarioPrincipal usuarioLogado,
                              RedirectAttributes redirectAttributes) {
        if (arquivo.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Por favor, selecione um arquivo para carregar.");
            return "redirect:/fac/painel";
        }
        try {
            avaliacaoService.criarNovaAvaliacaoAPartirDeXml(arquivo, usuarioLogado.getUsername());
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Nova avaliação carregada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao processar o arquivo: " + e.getMessage());
        }
        return "redirect:/fac/painel";
    }

    @GetMapping("/avaliacao/{id}")
    public String paginaDeAvaliacao(@PathVariable("id") Long id,
                                    @AuthenticationPrincipal UsuarioPrincipal usuarioLogado,
                                    Model model) {
        AvaliacaoDetalheDTO avaliacao = avaliacaoService.buscarAvaliacaoDetalhada(id, usuarioLogado.getUsername());
        model.addAttribute("avaliacao", avaliacao);
        return "fac/avaliacao";
    }
}