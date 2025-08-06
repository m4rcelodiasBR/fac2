package mb.cpo.facdigital.dto.avaliacao;

import mb.cpo.facdigital.model.enums.StatusAvaliacao;

import java.time.LocalDate;
import java.util.List;

public record AvaliacaoDetalheDTO(
        Long id,
        String eventoDescricao,
        LocalDate dataLimiteRemessa,
        StatusAvaliacao status,
        AvaliadorDTO avaliador,
        List<AvaliadoDTO> avaliados
) {
    public record AvaliadorDTO(
            String nome,
            String posto,
            String quadro,
            String fotoBase64
    ) {}

    public record AvaliadoDTO(
            Long id,
            String nip,
            String nome,
            String nomeDeGuerra,
            String antiguidade,
            String omSigla,
            String fotoBase64,
            String grau
    ) {}
}