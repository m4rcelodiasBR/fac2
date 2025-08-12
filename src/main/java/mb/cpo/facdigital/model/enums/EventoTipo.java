/**
 * Enum que representa os tipos de Evento de avaliação de forma autocontida.
 * Combina a estrutura e a lógica em uma única classe.
 * @author Marcelo Dias
 */
package mb.cpo.facdigital.model.enums;

import lombok.Getter;

@Getter
public enum EventoTipo {

    QAE(10, "QAE", "Quadro de Acesso por Escolha"),
    QAA_QAM(20, "QAA/QAM", "Quadro de Acesso por Antiguidade / Merecimento"),
    EC(30, "EC", "Escala de Comando");
    // Futuramente, adicionaremos todos os outros eventos aqui

    private final int codigo;
    private final String sigla;
    private final String descricao;

    EventoTipo(int codigo, String sigla, String descricao) {
        this.codigo = codigo;
        this.sigla = sigla;
        this.descricao = descricao;
    }

    /**
     * Método estático para encontrar um EventoTipo pelo seu código numérico.
     * @param codigo O código a ser procurado.
     * @return O Enum EventoTipo correspondente.
     * @throws IllegalArgumentException se o código não for encontrado.
     */
    public static EventoTipo porCodigo(int codigo) {
        for (EventoTipo tipo : values()) {
            if (tipo.codigo == codigo) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código de evento inválido: " + codigo);
    }
}