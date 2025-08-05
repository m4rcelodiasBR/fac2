package mb.cpo.facdigital.repository;

import mb.cpo.facdigital.model.entity.Avaliado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository para a entidade Avaliado.
 * Fornece métodos para interagir com a tabela 'avaliados'.
 * Geralmente, as operações serão feitas através da entidade mãe 'Avaliacao'.
 */
@Repository
public interface AvaliadoRepository extends JpaRepository<Avaliado, Long> {
    // Métodos de busca específicos para 'Avaliado' podem ser adicionados aqui se necessário.
}