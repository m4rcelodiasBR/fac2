package mb.cpo.facdigital.dto.avaliacao;

import java.util.List;

public record DadosXmlDTO(
        String nipAvaliador,
        String nomeAvaliador,
        String postoAvaliador,
        String quadroAvaliador,
        String dataLimite,
        int eventoCodigo,
        String eventoDataDescritiva,
        String situacaoPromocao,
        int numeroAditamento,
        List<AvaliadoXmlDTO> avaliados
) {
    public record AvaliadoXmlDTO(
            String nip,
            String nome,
            String nomeDeGuerra,
            int sequencia,
            String especialidade,
            String antiguidade,
            String posto,
            String quadro,
            String omSigla,
            String fotoBase64
    ) {}
}