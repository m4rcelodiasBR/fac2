/**
 * Interface para o serviço de negócio relacionado a Usuários.
 * Define o contrato para operações de usuário.
 * @author Marcelo Dias
 */
package mb.cpo.facdigital.service;

public interface UsuarioService {

    /**
     * Verifica a existência de um usuário pelo seu NIP.
     * @param nip O NIP a ser verificado.
     * @return `true` se o usuário existe, `false` caso contrário.
     */
    boolean verificaSeUsuarioExiste(String nip);

    /**
     * Cria um novo usuário
     * @param nip NIP do usuário
     * @param nome Nome do usuário
     * @param email E-mail do usuário
     * @param senha Senha criptografada do usuário
     */
    void criarNovoUsuario(String nip, String nome, String email, String senha);
}