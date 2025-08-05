package mb.cpo.facdigital.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

/**
 * Entidade que representa a tabela 'auditoria'.
 * Registra eventos importantes para rastreabilidade.
 */
@Data
@Entity
@Table(name = "auditoria")
public class LogAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(nullable = false)
    private String acao;

    @Lob
    private String detalhes;

    @Column(name = "ip_origem", length = 50)
    private String ipOrigem;

    @Column(name = "timestamp_acao", nullable = false, updatable = false)
    private OffsetDateTime timestampAcao;


    @PrePersist
    public void aoSalvar() {
        timestampAcao = OffsetDateTime.now();
    }
}