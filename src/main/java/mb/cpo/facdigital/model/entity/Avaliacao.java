package mb.cpo.facdigital.model.entity;

import jakarta.persistence.*;
import lombok.Data;
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
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_avaliador", nullable = false)
    private Usuario avaliador;

    @Column(name = "evento_codigo", nullable = false)
    private Integer eventoCodigo;

    @Column(name = "evento_data_descritiva", length = 50)
    private String eventoDataDescritiva;

    @Column(name = "evento_descricao", nullable = false)
    private String eventoDescricao;

    @Column(name = "evento_posto", length = 100)
    private String eventoPosto;

    @Column(name = "evento_quadro", length = 100)
    private String eventoQuadro;

    @Lob
    @Column(name = "situacao_promocao")
    private String situacaoPromocao;

    @Column(name = "numero_aditamento")
    private Integer numeroAditamento;

    @Column(name = "data_limite_remessa", nullable = false)
    private LocalDate dataLimiteRemessa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private StatusAvaliacao status;

    @Column(nullable = false)
    private Integer versao = 1;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_versao_anterior")
    private Avaliacao versaoAnterior;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private OffsetDateTime dataCriacao;

    @Column(name = "data_envio")
    private OffsetDateTime dataEnvio;

    @OneToMany(mappedBy = "avaliacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Avaliado> avaliados = new ArrayList<>();

    @PrePersist
    public void aoSalvar() {
        dataCriacao = OffsetDateTime.now();
    }
}