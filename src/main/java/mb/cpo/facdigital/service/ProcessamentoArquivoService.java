package mb.cpo.facdigital.service;

import mb.cpo.facdigital.model.entity.Avaliacao;
import mb.cpo.facdigital.model.entity.Usuario;
import org.springframework.web.multipart.MultipartFile;

public interface ProcessamentoArquivoService {
    /**
     * Processa um arquivo XML, extrai os dados e monta um objeto Avaliacao.
     * @param arquivo O arquivo XML enviado.
     * @param avaliador O usuário que está realizando o upload.
     * @return um objeto Avaliacao preenchido, pronto para ser salvo.
     */
    Avaliacao processarArquivoXml(MultipartFile arquivo, Usuario avaliador);
}