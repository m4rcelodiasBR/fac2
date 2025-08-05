package mb.cpo.facdigital.dto.avaliacao;

/**
 * Representa a estrutura de dados de um Avaliado que será serializada para JSON
 * e depois cifrada para ser salva no banco de dados.
 */
public record DadosAvaliadoDTO(
        String nip,
        String nome,
        String nomeDeGuerra,
        int sequencia,
        String especialidade,
        String antiguidade,
        String posto,
        String quadro,
        String omSigla,
        String fotoBase64,
        String grau
) {}