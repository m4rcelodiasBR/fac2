package mb.cpo.facdigital.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import mb.cpo.facdigital.model.entity.base.EntidadeBase;
import mb.cpo.facdigital.model.enums.CorpoQuadro;
import mb.cpo.facdigital.model.enums.PerfilUsuario;
import mb.cpo.facdigital.model.enums.Posto;

import java.time.OffsetDateTime;

/**
 * Entidade que representa a tabela 'usuarios'.
 * Armazena os dados de Avaliadores e Administradores.
 */
@Data
@Entity
@Table(name = "usuarios")
public class Usuario extends EntidadeBase {

    @Column(nullable = false, unique = true, length = 8)
    private String nip;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "senha_hash", nullable = false)
    private String senhaHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PerfilUsuario perfil;

    @Column(nullable = false)
    private boolean ativo;

    @Enumerated(EnumType.STRING)
    private Posto postoAvaliador;

    @Enumerated(EnumType.STRING)
    private CorpoQuadro quadroAvaliador;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String fotoAvaliadorBase64;
}