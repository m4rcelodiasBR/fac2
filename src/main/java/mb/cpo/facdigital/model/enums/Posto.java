package mb.cpo.facdigital.model.enums;

import lombok.Getter;

@Getter
public enum Posto {
    ALMIRANTE_DE_ESQUADRA("Almirante de Esquadra", "AE"),
    VICE_ALMIRANTE("Vice-Almirante", "VA"),
    CONTRA_ALMIRANTE("Contra-Almirante", "CA"),
    CAPITAO_DE_MAR_E_GUERRA("Capitão-de-Mar-e-Guerra", "CMG"),
    CAPITAO_DE_FRAGATA("Capitão-de-Fragata", "CF"),
    CAPITAO_DE_CORVETA("Capitão-de-Corveta", "CC"),
    CAPITAO_TENENTE("Capitão-Tenente", "CT"),
    PRIMEIRO_TENENTE("Primeiro-Tenente", "1T"),
    SEGUNDO_TENENTE("Segundo-Tenente", "2T");

    private final String descricao;
    private final String sigla;

    Posto(String descricao, String sigla) {
        this.descricao = descricao;
        this.sigla = sigla;
    }
}