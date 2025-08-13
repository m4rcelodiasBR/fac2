package mb.cpo.facdigital.dto.avaliacao;

import mb.cpo.facdigital.model.enums.StatusAvaliacao;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record AvaliacaoResumoDTO(
        Long id,
        String eventoSigla,
        String eventoDescricao,
        String eventoPosto,
        LocalDate dataLimiteRemessa,
        OffsetDateTime dataEnvio,
        StatusAvaliacao status
) {}