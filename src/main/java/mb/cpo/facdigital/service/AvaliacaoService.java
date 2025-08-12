package mb.cpo.facdigital.service;

import mb.cpo.facdigital.dto.avaliacao.AvaliacaoDetalheDTO;
import mb.cpo.facdigital.dto.avaliacao.AvaliacaoResumoDTO;
import mb.cpo.facdigital.dto.avaliacao.DadosXmlDTO;
import mb.cpo.facdigital.model.entity.Avaliacao;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface AvaliacaoService {

    /**
     * Processa um arquivo XML, valida o NIP do avaliador e cria uma nova avaliação no banco.
     * Este método é para usuários que já existem no sistema.
     * @param arquivoStream Stream do arquivo XML.
     * @param nipAvaliadorLogado NIP do usuário autenticado para validação.
     */
    void criarNovaAvaliacaoParaUsuarioExistente(InputStream arquivoStream, String nipAvaliadorLogado);

    /**
     * Apenas processa o arquivo XML e retorna os dados extraídos, sem salvar no banco.
     * Usado para o fluxo de primeiro acesso, para verificar se o usuário existe.
     * @param arquivo O arquivo XML.
     * @return um DTO com os dados lidos do XML.
     */
    DadosXmlDTO processarXmlParaPrimeiroAcesso(MultipartFile arquivo);

    List<AvaliacaoResumoDTO> listarAvaliacoesPorAvaliador(String nipAvaliador);

    AvaliacaoDetalheDTO buscarAvaliacaoDetalhada(Long idAvaliacao, String nipAvaliador);

    void atualizarGrau(Long idAvaliacao, Long idAvaliado, String grau, String nipAvaliador);

    void enviarAvaliacao(Long idAvaliacao, String nipAvaliador);

}