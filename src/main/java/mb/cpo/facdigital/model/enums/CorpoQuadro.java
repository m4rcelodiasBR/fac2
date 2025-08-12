package mb.cpo.facdigital.model.enums;

import lombok.Getter;

@Getter
public enum CorpoQuadro {
    CA("Corpo da Armada"),
    QCCA("Quadro Complementar de Oficiais da Armada"),
    T("Quadro Técnico"),
    CN("Quadro de Capelães Navais"),
    AA("Quadro Auxiliar da Armada"),
    AFN("Quadro Auxiliar de Fuzileiros Navais"),
    EN("Corpo de Engenheiros da Marinha"),
    IM("Corpo de Intendentes da Marinha"),
    QCIM("Quadro Complementar de Intendentes da Marinha"),
    MD("Corpo de Médicos"),
    CD("Quadro de Cirurgiões Dentistas"),
    S("Quadro de Apoio a Saúde"),
    FN("Corpo de Fuzileiros Navais"),
    QCFN("Quadro Complementar de Oficiais Fuzileiros Navais");

    private final String descricao;

    CorpoQuadro(String descricao) {
        this.descricao = descricao;
    }
}