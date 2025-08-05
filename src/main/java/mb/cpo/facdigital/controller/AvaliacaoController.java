package mb.cpo.facdigital.controller;

import mb.cpo.facdigital.dto.avaliacao.AvaliacaoDetalheDTO;
import mb.cpo.facdigital.dto.avaliacao.AvaliacaoResumoDTO;
import mb.cpo.facdigital.dto.avaliacao.PedidoAtualizacaoGrauDTO;
import mb.cpo.facdigital.model.entity.Avaliacao;
import mb.cpo.facdigital.security.UsuarioPrincipal;
import mb.cpo.facdigital.service.AvaliacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @GetMapping("/minhas")
    public ResponseEntity<List<AvaliacaoResumoDTO>> listarMinhasAvaliacoes(
            @AuthenticationPrincipal UsuarioPrincipal usuarioLogado) {
        List<AvaliacaoResumoDTO> lista = avaliacaoService.listarAvaliacoesPorAvaliador(usuarioLogado.getUsername());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{idAvaliacao}")
    public ResponseEntity<AvaliacaoDetalheDTO> buscarAvaliacaoPorId(
            @PathVariable Long idAvaliacao,
            @AuthenticationPrincipal UsuarioPrincipal usuarioLogado) {
        AvaliacaoDetalheDTO dto = avaliacaoService.buscarAvaliacaoDetalhada(idAvaliacao, usuarioLogado.getUsername());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{idAvaliacao}/avaliados/{idAvaliado}/grau")
    public ResponseEntity<Void> atualizarGrau(
            @PathVariable Long idAvaliacao,
            @PathVariable Long idAvaliado,
            @RequestBody PedidoAtualizacaoGrauDTO pedido,
            @AuthenticationPrincipal UsuarioPrincipal usuarioLogado) {

        avaliacaoService.atualizarGrau(idAvaliacao, idAvaliado, pedido.grau(), usuarioLogado.getUsername());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{idAvaliacao}/enviar")
    public ResponseEntity<Void> enviarAvaliacao(
            @PathVariable Long idAvaliacao,
            @AuthenticationPrincipal UsuarioPrincipal usuarioLogado) {

        avaliacaoService.enviarAvaliacao(idAvaliacao, usuarioLogado.getUsername());
        return ResponseEntity.ok().build();
    }
}