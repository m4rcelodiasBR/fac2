package mb.cpo.facdigital.model.enums;

import lombok.Getter;

@Getter
public enum Especialidade {
    AVN("Aperfeiçoamento de Aviação para Oficiais"),
    HN("Aperfeiçoamento de Hidrografia para Oficiais"),
    SB("Aperfeiçoamento de Submarinos para Oficiais"),
    MEC("Mergulhador de Combate"),
    QTE("Qualificação Técnica");

    private final String descricao;

    Especialidade(String descricao) {
        this.descricao = descricao;
    }
}