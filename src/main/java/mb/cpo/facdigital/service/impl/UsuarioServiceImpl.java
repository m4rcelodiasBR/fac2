/**
 * Implementação do serviço de negócio para Usuários.
 * @author Marcelo Dias
 */
package mb.cpo.facdigital.service.impl;

import lombok.RequiredArgsConstructor;
import mb.cpo.facdigital.model.entity.Usuario;
import mb.cpo.facdigital.model.enums.PerfilUsuario;
import mb.cpo.facdigital.repository.UsuarioRepository;
import mb.cpo.facdigital.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean verificaSeUsuarioExiste(String nip) {
        return usuarioRepository.findByNip(nip).isPresent();
    }

    @Override
    @Transactional
    public void criarNovoUsuario(String nip, String nome, String email, String senha) {
        if (usuarioRepository.findByNip(nip).isPresent()) {
            throw new IllegalStateException("Usuário com este NIP já existe.");
        }
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNip(nip);
        novoUsuario.setNome(nome);
        novoUsuario.setEmail(email);
        novoUsuario.setSenhaHash(passwordEncoder.encode(senha));
        novoUsuario.setPerfil(PerfilUsuario.ROLE_AVALIADOR);
        novoUsuario.setAtivo(true);
        usuarioRepository.save(novoUsuario);
    }
}