package mb.cpo.facdigital.repository;

import mb.cpo.facdigital.model.entity.Avaliacao;
import mb.cpo.facdigital.model.enums.StatusAvaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para a entidade Avaliacao.
 * Fornece métodos para interagir com a tabela 'avaliacoes'.
 */
@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    /**
     * Busca todas as avaliações associadas a um determinado avaliador,
     * ordenadas pela data de criação de forma descendente.
     *
     * @param idAvaliador O ID do usuário avaliador.
     * @return Uma lista de avaliações.
     */
    List<Avaliacao> findByAvaliadorIdOrderByDataCriacaoDesc(Long idAvaliador);

    /**
     * Busca todas as avaliações de um avaliador com um status específico.
     *
     * @param idAvaliador O ID do usuário avaliador.
     * @param status O status da avaliação a ser buscado.
     * @return Uma lista de avaliações que correspondem aos critérios.
     */
    List<Avaliacao> findByAvaliadorIdAndStatus(Long idAvaliador, StatusAvaliacao status);

}