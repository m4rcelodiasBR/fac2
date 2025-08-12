package mb.cpo.facdigital.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import mb.cpo.facdigital.model.entity.base.EntidadeBase;

@Entity
@Table(name = "avaliados")
@Data
public class Avaliado extends EntidadeBase {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_avaliacao", nullable = false)
    private Avaliacao avaliacao;

    @Lob
    @Column(name = "dados_cifrados", nullable = false, columnDefinition = "TEXT")
    private String dadosCifrados;
}