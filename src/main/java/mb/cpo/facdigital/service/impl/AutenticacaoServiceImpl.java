package mb.cpo.facdigital.service.impl;

import mb.cpo.facdigital.dto.autenticacao.PedidoLoginDTO;
import mb.cpo.facdigital.dto.autenticacao.RespostaTokenDTO;
import mb.cpo.facdigital.security.jwt.ProvedorTokenJwt;
import mb.cpo.facdigital.service.AutenticacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoServiceImpl implements AutenticacaoService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ProvedorTokenJwt provedorTokenJwt;

    @Override
    public RespostaTokenDTO autenticarUsuario(PedidoLoginDTO pedidoLoginDTO) {
        // Cria um objeto de autenticação com as credenciais fornecidas
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        pedidoLoginDTO.nip(),
                        pedidoLoginDTO.senha()
                )
        );

        // Armazena a autenticação no contexto de segurança do Spring
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Gera o token JWT
        String token = provedorTokenJwt.gerarToken(authentication);

        return new RespostaTokenDTO(pedidoLoginDTO.nip(), token);
    }
}