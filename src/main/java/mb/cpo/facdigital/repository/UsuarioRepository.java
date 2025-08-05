package mb.cpo.facdigital.repository;

import mb.cpo.facdigital.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository para a entidade Usuario.
 * Fornece métodos para interagir com a tabela 'usuarios' no banco de dados.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca um usuário pelo seu NIP.
     * O Optional é usado para indicar que o resultado pode não existir,
     * evitando NullPointerExceptions.
     *
     * @param nip O NIP do usuário a ser buscado.
     * @return um Optional contendo o usuário se encontrado, ou vazio caso contrário.
     */
    Optional<Usuario> findByNip(String nip);

}