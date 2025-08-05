package mb.cpo.facdigital.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import mb.cpo.facdigital.model.entity.Avaliacao;

/**
 * Entidade que representa a tabela 'avaliados'.
 * Armazena os dados de um militar a ser avaliado dentro de uma FAC.
 */
@Data
@Entity
@Table(name = "avaliados")
public class Avaliado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_avaliacao", nullable = false)
    private Avaliacao avaliacao;

    @Column(name = "nip_avaliado", nullable = false, length = 8)
    private String nipAvaliado;

    @Column(name = "nome_avaliado", nullable = false)
    private String nomeAvaliado;

    @Column(name = "nome_de_guerra", length = 100)
    private String nomeDeGuerra;

    private Integer sequencia;

    @Column(length = 100)
    private String especialidade;

    @Column(length = 10)
    private String antiguidade;

    @Column(length = 100)
    private String posto;

    @Column(length = 100)
    private String quadro;

    @Column(name = "om_sigla", length = 50)
    private String omSigla;

    @Lob
    @Column(name = "foto_avaliado_base64")
    private String fotoAvaliadoBase64;

    @Lob
    @Column(name = "grau_criptografado")
    private String grauCriptografado;
}