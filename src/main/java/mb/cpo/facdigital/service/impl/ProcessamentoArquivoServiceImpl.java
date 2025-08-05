package mb.cpo.facdigital.service.impl;

import mb.cpo.facdigital.dto.avaliacao.DadosXmlDTO;
import mb.cpo.facdigital.model.entity.Avaliacao;
import mb.cpo.facdigital.model.entity.Avaliado;
import mb.cpo.facdigital.model.entity.Usuario;
import mb.cpo.facdigital.model.enums.StatusAvaliacao;
import mb.cpo.facdigital.service.ProcessamentoArquivoService;
import mb.cpo.facdigital.util.UtilitarioParserXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.List;

@Service
public class ProcessamentoArquivoServiceImpl implements ProcessamentoArquivoService {

    @Autowired
    private UtilitarioParserXML utilitarioParserXML;

    @Override
    public Avaliacao processarArquivoXml(MultipartFile arquivo, Usuario avaliador) {
        try {
            DadosXmlDTO dadosXml = utilitarioParserXML.extrairDadosDoXml(arquivo);

            // Validação de segurança: o NIP do XML deve ser o mesmo do usuário logado.
            if (!dadosXml.nipAvaliador().equals(avaliador.getNip())) {
                throw new SecurityException("O NIP do arquivo XML não corresponde ao do avaliador autenticado.");
            }

            Avaliacao avaliacao = new Avaliacao();
            avaliacao.setAvaliador(avaliador);
            avaliacao.setStatus(StatusAvaliacao.INICIADA);

            // Mapeia dados do evento
            avaliacao.setEventoCodigo(dadosXml.eventoCodigo());
            avaliacao.setEventoDataDescritiva(dadosXml.eventoDataDescritiva());
            // A descrição do evento pode ser buscada de uma tabela de domínio no futuro
            avaliacao.setEventoDescricao("Evento " + dadosXml.eventoCodigo());
            avaliacao.setSituacaoPromocao(dadosXml.situacaoPromocao());
            avaliacao.setNumeroAditamento(dadosXml.numeroAditamento());
            avaliacao.setDataLimiteRemessa(LocalDate.parse(dadosXml.dataLimite(), DateTimeFormatter.ISO_LOCAL_DATE));

            // Mapeia a lista de avaliados
            List<Avaliado> listaAvaliados = dadosXml.avaliados().stream()
                    .map(dto -> mapearAvaliadoDtoParaEntidade(dto, avaliacao))
                    .collect(Collectors.toList());

            avaliacao.setAvaliados(listaAvaliados);

            return avaliacao;

        } catch (Exception e) {
            // Aqui podemos lançar uma exceção customizada
            throw new RuntimeException("Erro ao processar o arquivo XML: " + e.getMessage(), e);
        }
    }

    private Avaliado mapearAvaliadoDtoParaEntidade(DadosXmlDTO.AvaliadoXmlDTO dto, Avaliacao avaliacaoPai) {
        Avaliado entidade = new Avaliado();
        entidade.setAvaliacao(avaliacaoPai);
        entidade.setNipAvaliado(dto.nip());
        entidade.setNomeAvaliado(dto.nome());
        entidade.setNomeDeGuerra(dto.nomeDeGuerra());
        entidade.setSequencia(dto.sequencia());
        entidade.setEspecialidade(dto.especialidade());
        entidade.setAntiguidade(dto.antiguidade());
        entidade.setPosto(dto.posto());
        entidade.setQuadro(dto.quadro());
        entidade.setOmSigla(dto.omSigla());
        entidade.setFotoAvaliadoBase64(dto.fotoBase64());
        return entidade;
    }
}