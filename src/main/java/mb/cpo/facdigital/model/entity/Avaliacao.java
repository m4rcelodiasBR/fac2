package mb.cpo.facdigital.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import mb.cpo.facdigital.model.entity.base.EntidadeBase;
import mb.cpo.facdigital.model.enums.CorpoQuadro;
import mb.cpo.facdigital.model.enums.EventoTipo;
import mb.cpo.facdigital.model.enums.Posto;
import mb.cpo.facdigital.model.enums.StatusAvaliacao;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa a tabela 'avaliacoes'.
 * Cada registro é uma instância de uma FAC.
 */
@Data
@Entity
@Table(name = "avaliacoes")
public class Avaliacao extends EntidadeBase {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAvaliacao status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avaliador_id", nullable = false)
    private Usuario avaliador;

    @Column(nullable = false)
    private LocalDate dataLimiteRemessa;

    private OffsetDateTime dataEnvio;
    private String situacaoPromocao;
    private int numeroAditamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "evento_tipo", nullable = false)
    private EventoTipo eventoTipo;

    @Column(name = "evento_data_descritiva")
    private String eventoDataDescritiva;

    @Enumerated(EnumType.STRING)
    @Column(name = "evento_posto")
    private Posto eventoPosto;

    @Enumerated(EnumType.STRING)
    @Column(name = "evento_quadro")
    private CorpoQuadro eventoQuadro;

    @OneToMany(mappedBy = "avaliacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Avaliado> avaliados = new ArrayList<>();
}