package mb.cpo.facdigital.dto.avaliacao;

import jakarta.validation.constraints.NotBlank;

public record PedidoAtualizacaoGrauDTO(
        @NotBlank(message = "O grau não pode ser vazio.")
        String grau
) {}