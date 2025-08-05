package mb.cpo.facdigital.service;

import mb.cpo.facdigital.dto.autenticacao.PedidoLoginDTO;
import mb.cpo.facdigital.dto.autenticacao.RespostaTokenDTO;

/**
 * Contrato para os serviços de autenticação.
 */
public interface AutenticacaoService {

    /**
     * Autentica um usuário com NIP e senha e retorna um token JWT.
     * @param pedidoLoginDTO Objeto contendo as credenciais.
     * @return um DTO com o token de acesso.
     */
    RespostaTokenDTO autenticarUsuario(PedidoLoginDTO pedidoLoginDTO);
}