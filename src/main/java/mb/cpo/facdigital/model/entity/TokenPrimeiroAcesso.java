package mb.cpo.facdigital.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import mb.cpo.facdigital.model.entity.base.EntidadeBase;
import java.time.OffsetDateTime;

@Entity
@Table(name = "tokens_primeiro_acesso")
@Data
public class TokenPrimeiroAcesso extends EntidadeBase {

    @Column(nullable = false, unique = true)
    private String nipAvaliador;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private OffsetDateTime dataExpiracao;

    @Column(nullable = false)
    private String email;
}