package mb.cpo.facdigital.controller;

import mb.cpo.facdigital.model.entity.Avaliacao;
import mb.cpo.facdigital.security.UsuarioPrincipal;
import mb.cpo.facdigital.service.AvaliacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/fac/avaliacao")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    @PostMapping("/carregar-xml")
    public ResponseEntity<?> carregarXml(
            @RequestParam("arquivo") MultipartFile arquivo,
            @AuthenticationPrincipal UsuarioPrincipal usuarioLogado) {

        // O @AuthenticationPrincipal injeta o usuário que foi validado pelo nosso Filtro JWT.
        // Isso garante que só o dono do token pode executar esta ação.
        String nipAvaliador = usuarioLogado.getUsername();

        try {
            Avaliacao novaAvaliacao = avaliacaoService.criarNovaAvaliacaoAPartirDeXml(arquivo, nipAvaliador);
            // No futuro, retornaremos um DTO aqui, mas por enquanto a entidade é suficiente para teste.
            return new ResponseEntity<>(novaAvaliacao, HttpStatus.CREATED);
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN); // 403 Forbidden
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao processar arquivo: " + e.getMessage(), HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }
}