package mb.cpo.facdigital.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import mb.cpo.facdigital.dto.avaliacao.AvaliacaoDetalheDTO;
import mb.cpo.facdigital.dto.avaliacao.AvaliacaoResumoDTO;
import mb.cpo.facdigital.dto.avaliacao.DadosXmlDTO;
import mb.cpo.facdigital.security.UsuarioPrincipal;
import mb.cpo.facdigital.service.AvaliacaoService;
import mb.cpo.facdigital.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/fac")
@RequiredArgsConstructor
public class FacController {

    private final AvaliacaoService avaliacaoService;
    private final UsuarioService usuarioService;

    @GetMapping("/painel")
    public String painelDoAvaliador(@AuthenticationPrincipal UsuarioPrincipal usuarioLogado, Model model) {
        String nipAvaliador = usuarioLogado.getUsername();
        List<AvaliacaoResumoDTO> avaliacoes = avaliacaoService.listarAvaliacoesPorAvaliador(nipAvaliador);
        model.addAttribute("avaliacoes", avaliacoes);
        return "fac/painel";
    }

    @PostMapping("/carregar-xml")
    public String carregarXml(@RequestParam("arquivoXml") MultipartFile arquivoXml,
                              RedirectAttributes redirectAttributes,
                              HttpSession session) {
        if (arquivoXml.isEmpty() || !Objects.equals(arquivoXml.getContentType(), "text/xml")) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Arquivo inválido. Por favor, envie um arquivo .xml.");
            return "redirect:/fac/painel";
        }

        try {
            DadosXmlDTO dadosXml = avaliacaoService.processarXmlParaPrimeiroAcesso(arquivoXml);

            boolean usuarioExiste = usuarioService.verificaSeUsuarioExiste(dadosXml.nipAvaliador());

            if (usuarioExiste) {
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Usuário já existe. Avaliação para NIP " + dadosXml.nipAvaliador() + " criada!");
             } else {
                session.setAttribute("dadosXmlPrimeiroAcesso", dadosXml);
                return "redirect:/primeiro-acesso/informar-email";
            }
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

    /**
     * Atualiza o grau de um único avaliado de forma assíncrona (AJAX).
     * @param avaliacaoId O ID da avaliação pai.
     * @param avaliadoId O ID do avaliado a ser atualizado.
     * @param grau O novo grau a ser salvo.
     * @return Retorna um status HTTP 200 (OK) em caso de sucesso ou 400 (Bad Request) em caso de erro.
     */
    /**
     * Atualiza o grau de um único avaliado (chamada via AJAX).
     */
    @PostMapping("/avaliacao/{avaliacaoId}/avaliado/{avaliadoId}")
    @ResponseBody
    public ResponseEntity<Void> atualizarGrau(@PathVariable Long avaliacaoId,
                                              @PathVariable Long avaliadoId,
                                              @RequestParam String grau,
                                              Principal principal) { // Adicionado Principal para segurança
        try {
            avaliacaoService.atualizarGrau(avaliacaoId, avaliadoId, grau, principal.getName());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Finaliza e envia a avaliação, mudando seu status para CONCLUIDA.
     * @param id O ID da avaliação a ser finalizada.
     * @param redirectAttributes Atributos para passar mensagens de sucesso.
     * @return Redireciona o usuário de volta para o painel.
     */
    @PostMapping("/avaliacao/{id}/enviar")
    public String enviarAvaliacao(@PathVariable Long id,
                                  Principal principal,
                                  RedirectAttributes redirectAttributes) {
        try {
            avaliacaoService.enviarAvaliacao(id, principal.getName());
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Avaliação #" + id + " foi enviada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao enviar avaliação: " + e.getMessage());
            return "redirect:/fac/avaliacao/" + id;
        }
        return "redirect:/fac/painel";
    }
}