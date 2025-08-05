package mb.cpo.facdigital.controller;

import mb.cpo.facdigital.dto.autenticacao.PedidoLoginDTO;
import mb.cpo.facdigital.dto.autenticacao.RespostaTokenDTO;
import mb.cpo.facdigital.service.AutenticacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller que expõe os endpoints de autenticação.
 */
@RestController
@RequestMapping("/fac/autenticacao")
public class AutenticacaoController {

    @Autowired
    private AutenticacaoService autenticacaoService;

    @PostMapping("/login")
    public ResponseEntity<RespostaTokenDTO> autenticar(@RequestBody PedidoLoginDTO pedidoLoginDTO) {
        RespostaTokenDTO resposta = autenticacaoService.autenticarUsuario(pedidoLoginDTO);
        return ResponseEntity.ok(resposta);
    }

    // Endpoints para /primeiro-acesso/iniciar e /primeiro-acesso/validar serão adicionados aqui.
}