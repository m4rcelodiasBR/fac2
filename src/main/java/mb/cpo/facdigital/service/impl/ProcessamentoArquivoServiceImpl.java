package mb.cpo.facdigital.service.impl;

import mb.cpo.facdigital.dto.avaliacao.DadosXmlDTO;
import mb.cpo.facdigital.service.ProcessamentoArquivoService;
import mb.cpo.facdigital.util.UtilitarioParserXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class ProcessamentoArquivoServiceImpl implements ProcessamentoArquivoService {

    @Autowired
    private UtilitarioParserXML utilitarioParserXML;

    @Override
    public DadosXmlDTO processarArquivoXml(InputStream arquivo) {
        try {
            return utilitarioParserXML.extrairDadosDoXml(arquivo);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao analisar o arquivo XML.", e);
        }
    }
}