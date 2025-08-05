package mb.cpo.facdigital.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entidade que representa a tabela 'avaliados'.
 * Agora, armazena todos os dados do militar de forma cifrada em um único campo.
 */
@Data
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "avaliados")
public class Avaliado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_avaliacao", nullable = false)
    private Avaliacao avaliacao;

    /**
     * Campo principal que armazena um JSON contendo todos os dados do avaliado
     * (NIP, nome, grau, etc.), cifrado como uma única string.
     */
    @Lob
    @Column(name = "dados_cifrados", nullable = false)
    private String dadosCifrados;
}