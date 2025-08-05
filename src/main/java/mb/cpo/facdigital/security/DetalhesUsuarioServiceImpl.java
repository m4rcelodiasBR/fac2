package mb.cpo.facdigital.security;

import mb.cpo.facdigital.model.entity.Usuario;
import mb.cpo.facdigital.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Serviço que busca os dados de um usuário no banco para o Spring Security.
 */
@Service
public class DetalhesUsuarioServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String nip) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByNip(nip)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário com NIP " + nip + " não encontrado."));

        return new UsuarioPrincipal(usuario);
    }
}