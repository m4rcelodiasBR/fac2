package mb.cpo.facdigital.config;

import lombok.RequiredArgsConstructor;
import mb.cpo.facdigital.model.entity.Usuario;
import mb.cpo.facdigital.model.enums.PerfilUsuario;
import mb.cpo.facdigital.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Classe executada na inicialização da aplicação para garantir
 * que um usuário Administrador padrão exista no banco de dados.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Define o NIP do usuário administrador
        final String adminNip = "99999999";

        // Verifica se o usuário já existe no banco para evitar duplicatas
        if (usuarioRepository.findByNip(adminNip).isEmpty()) {

            System.out.println(">>> Criando usuário Administrador padrão...");

            Usuario admin = new Usuario();
            admin.setNip(adminNip);
            admin.setNome("Administrador do Sistema");
            admin.setEmail("administrador@marinha.mil.br"); // E-mail de exemplo

            // Cifra a senha antes de salvar
            admin.setSenhaHash(passwordEncoder.encode("marinha"));

            admin.setPerfil(PerfilUsuario.ROLE_ADMIN);
            admin.setAtivo(true);

            usuarioRepository.save(admin);

            System.out.println(">>> Usuário Administrador criado com sucesso!");
            System.out.println(">>> NIP: " + adminNip);
            System.out.println(">>> Senha: marinha");
        }
    }
}