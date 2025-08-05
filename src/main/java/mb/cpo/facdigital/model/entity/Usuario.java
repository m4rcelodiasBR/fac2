package mb.cpo.facdigital.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import mb.cpo.facdigital.model.enums.PerfilUsuario;

import java.time.OffsetDateTime;

/**
 * Entidade que representa a tabela 'usuarios'.
 * Armazena os dados de Avaliadores e Administradores.
 */
@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 8)
    private String nip;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "senha_hash", nullable = false)
    private String senhaHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private PerfilUsuario perfil;

    @Column(name = "posto_avaliador", length = 100)
    private String postoAvaliador;

    @Column(name = "quadro_avaliador", length = 100)
    private String quadroAvaliador;

    @Lob // Large Object - para textos longos como Base64
    @Column(name = "foto_avaliador_base64")
    private String fotoAvaliadorBase64;

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private OffsetDateTime dataCriacao;

    @PrePersist
    public void aoSalvar() {
        dataCriacao = OffsetDateTime.now();
    }
}