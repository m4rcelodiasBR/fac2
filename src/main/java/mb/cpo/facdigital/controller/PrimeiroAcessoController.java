package mb.cpo.facdigital.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import mb.cpo.facdigital.dto.DadosPrimeiroAcessoDTO;
import mb.cpo.facdigital.dto.avaliacao.DadosXmlDTO;
import mb.cpo.facdigital.service.PrimeiroAcessoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.OffsetDateTime;

@Controller
@RequestMapping("/primeiro-acesso")
@RequiredArgsConstructor
public class PrimeiroAcessoController {

    private final PrimeiroAcessoService primeiroAcessoService;

    @GetMapping("/informar-email")
    public String paginaInformarEmail(
            @SessionAttribute(name = "dadosXmlPrimeiroAcesso", required = false) DadosXmlDTO dadosXml,
            Model model) {
        if (dadosXml == null) {
            return "redirect:/";
        }
        model.addAttribute("dadosXml", dadosXml);
        return "primeiro-acesso/informar-email";
    }

    @PostMapping("/enviar-token")
    public String enviarToken(@RequestParam String email,
                              @SessionAttribute("dadosXmlPrimeiroAcesso") DadosXmlDTO dadosXml,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        try {
            session.setAttribute("emailPrimeiroAcesso", email);

            primeiroAcessoService.iniciarProcesso(dadosXml.nipAvaliador(), email);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Token enviado para " + email + ". Verifique sua caixa de entrada.");
            return "redirect:/primeiro-acesso/validar-token";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao enviar e-mail: " + e.getMessage());
            return "redirect:/primeiro-acesso/informar-email";
        }
    }

    @GetMapping("/validar-token")
    public String paginaValidarToken(
            @SessionAttribute("dadosXmlPrimeiroAcesso") DadosXmlDTO dadosXml,
            @SessionAttribute("emailPrimeiroAcesso") String email,
            Model model) {

        DadosPrimeiroAcessoDTO dto = new DadosPrimeiroAcessoDTO();
        dto.setNip(dadosXml.nipAvaliador());
        dto.setNome(dadosXml.nomeAvaliador());
        dto.setEmail(email);
        model.addAttribute("dadosPrimeiroAcesso", dto);
        return "primeiro-acesso/validar-token";
    }

    @PostMapping("/finalizar")
    public String finalizarCadastro(@ModelAttribute DadosPrimeiroAcessoDTO dados,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        try {
            primeiroAcessoService.finalizarProcesso(dados);
            session.removeAttribute("dadosXmlPrimeiroAcesso");
            session.removeAttribute("emailPrimeiroAcesso");
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Cadastro realizado com sucesso! Por favor, faça o login.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao finalizar cadastro: " + e.getMessage());
            return "redirect:/primeiro-acesso/validar-token";
        }
    }
}