package mb.cpo.facdigital.repository;

import mb.cpo.facdigital.model.entity.LogAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository para a entidade LogAuditoria.
 * Fornece métodos para interagir com a tabela 'auditoria'.
 */
@Repository
public interface LogAuditoriaRepository extends JpaRepository<LogAuditoria, Long> {
    // Métodos de busca para logs de auditoria (ex: por usuário, por período) podem ser adicionados aqui.
}