package mb.cpo.facdigital.service;

import mb.cpo.facdigital.dto.avaliacao.DadosXmlDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ProcessamentoArquivoService {
    /**
     * Processa um arquivo XML e extrai os dados para um DTO.
     * @param arquivo O arquivo XML enviado.
     * @return um DTO contendo os dados brutos extraídos do XML.
     */
    DadosXmlDTO processarArquivoXml(MultipartFile arquivo);
}