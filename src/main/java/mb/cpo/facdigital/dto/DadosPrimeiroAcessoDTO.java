/**
 * DTO para transportar os dados do formulário de finalização de cadastro do primeiro acesso.
 * @author Marcelo Dias
 */
package mb.cpo.facdigital.dto;

import lombok.Data;

@Data
public class DadosPrimeiroAcessoDTO {
    private String nip;
    private String nome;
    private String email;
    private String token;
    private String senha;
}