package mb.cpo.facdigital.service;

import mb.cpo.facdigital.model.entity.Avaliacao;
import org.springframework.web.multipart.MultipartFile;

public interface AvaliacaoService {
    /**
     * Orquestra a criação de uma nova avaliação a partir de um arquivo XML.
     * @param arquivo O arquivo XML enviado.
     * @param nipAvaliador O NIP do usuário autenticado que está realizando a ação.
     * @return A entidade Avaliacao que foi criada e salva no banco.
     */
    Avaliacao criarNovaAvaliacaoAPartirDeXml(MultipartFile arquivo, String nipAvaliador);
}