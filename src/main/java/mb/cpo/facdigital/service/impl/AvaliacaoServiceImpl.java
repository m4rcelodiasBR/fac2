/**
 * Implementação do serviço de negócio para Avaliações (FAC).
 * Refatorado para usar o modelo de dados completo com Enums.
 * @author Marcelo Dias
 */
package mb.cpo.facdigital.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import mb.cpo.facdigital.dto.avaliacao.AvaliacaoDetalheDTO;
import mb.cpo.facdigital.dto.avaliacao.AvaliacaoResumoDTO;
import mb.cpo.facdigital.dto.avaliacao.DadosAvaliadoDTO;
import mb.cpo.facdigital.dto.avaliacao.DadosXmlDTO;
import mb.cpo.facdigital.model.entity.Avaliacao;
import mb.cpo.facdigital.model.entity.Avaliado;
import mb.cpo.facdigital.model.entity.Usuario;
import mb.cpo.facdigital.model.enums.*;
import mb.cpo.facdigital.repository.AvaliacaoRepository;
import mb.cpo.facdigital.repository.AvaliadoRepository;
import mb.cpo.facdigital.repository.UsuarioRepository;
import mb.cpo.facdigital.service.AvaliacaoService;
import mb.cpo.facdigital.service.ProcessamentoArquivoService;
import mb.cpo.facdigital.util.UtilitarioCriptografia;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvaliacaoServiceImpl implements AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final AvaliadoRepository avaliadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProcessamentoArquivoService processamentoArquivoService;
    private final UtilitarioCriptografia utilitarioCriptografia;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void criarNovaAvaliacaoParaUsuarioExistente(InputStream arquivoStream, String nipAvaliadorLogado) {
        Usuario avaliador = usuarioRepository.findByNip(nipAvaliadorLogado)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + nipAvaliadorLogado));

        DadosXmlDTO dadosXml = processamentoArquivoService.processarArquivoXml(arquivoStream);

        if (!dadosXml.nipAvaliador().equals(avaliador.getNip())) {
            throw new SecurityException("O NIP do arquivo XML não corresponde ao do avaliador autenticado.");
        }

        avaliador.setPostoAvaliador(Posto.valueOf(dadosXml.postoAvaliador()));
        avaliador.setQuadroAvaliador(CorpoQuadro.valueOf(dadosXml.quadroAvaliador()));
        usuarioRepository.save(avaliador);

        EventoTipo tipoDeEvento = EventoTipo.porCodigo(dadosXml.eventoCodigo());

        Avaliacao novaAvaliacao = new Avaliacao();
        novaAvaliacao.setAvaliador(avaliador);
        novaAvaliacao.setEventoTipo(tipoDeEvento); // Usa o Enum
        novaAvaliacao.setEventoDataDescritiva(dadosXml.eventoDataDescritiva());
        // TODO: Mapear Posto e CorpoQuadro do Evento a partir do XML
        // novaAvaliacao.setEventoPosto(...);
        // novaAvaliacao.setEventoQuadro(...);
        novaAvaliacao.setStatus(StatusAvaliacao.INICIADA);
        novaAvaliacao.setSituacaoPromocao(dadosXml.situacaoPromocao());
        novaAvaliacao.setNumeroAditamento(dadosXml.numeroAditamento());
        novaAvaliacao.setDataLimiteRemessa(LocalDate.parse(dadosXml.dataLimite(), DateTimeFormatter.ISO_LOCAL_DATE));

        List<Avaliado> listaAvaliados = dadosXml.avaliados().stream()
                .map(avaliadoXmlDto -> {
                    try {
                        DadosAvaliadoDTO dadosEmClaro = new DadosAvaliadoDTO(
                                avaliadoXmlDto.nip(), avaliadoXmlDto.nome(), avaliadoXmlDto.nomeDeGuerra(),
                                avaliadoXmlDto.sequencia(), avaliadoXmlDto.especialidade(), avaliadoXmlDto.antiguidade(),
                                avaliadoXmlDto.posto(), avaliadoXmlDto.quadro(), avaliadoXmlDto.omSigla(),
                                avaliadoXmlDto.fotoBase64(), ""
                        );
                        String dadosJson = objectMapper.writeValueAsString(dadosEmClaro);
                        String dadosCifrados = utilitarioCriptografia.cifrar(dadosJson);
                        Avaliado entidadeFinal = new Avaliado();
                        entidadeFinal.setAvaliacao(novaAvaliacao);
                        entidadeFinal.setDadosCifrados(dadosCifrados);
                        return entidadeFinal;
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("Erro ao serializar dados do avaliado.", e);
                    }
                })
                .collect(Collectors.toList());

        novaAvaliacao.setAvaliados(listaAvaliados);
        avaliacaoRepository.save(novaAvaliacao);
    }

    @Override
    public DadosXmlDTO processarXmlParaPrimeiroAcesso(MultipartFile arquivo) {
        try {
            return processamentoArquivoService.processarArquivoXml(arquivo.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvaliacaoResumoDTO> listarAvaliacoesPorAvaliador(String nipAvaliador) {
        Usuario avaliador = usuarioRepository.findByNip(nipAvaliador)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + nipAvaliador));

        List<Avaliacao> avaliacoes = avaliacaoRepository.findByAvaliadorIdOrderByDataCriacaoDesc(avaliador.getId());

        return avaliacoes.stream()
                .map(avaliacao -> new AvaliacaoResumoDTO(
                        avaliacao.getId(),
                        avaliacao.getEventoTipo().getSigla(),
                        avaliacao.getEventoTipo().getDescricao(),
                        avaliacao.getEventoPosto().getDescricao(),
                        avaliacao.getDataLimiteRemessa(),
                        avaliacao.getDataEnvio(),
                        avaliacao.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AvaliacaoDetalheDTO buscarAvaliacaoDetalhada(Long idAvaliacao, String nipAvaliador) {
        Avaliacao avaliacao = buscarEValidarAcesso(idAvaliacao, nipAvaliador);
        return mapearParaDetalheDTO(avaliacao);
    }

    @Override
    @Transactional
    public void atualizarGrau(Long idAvaliacao, Long idAvaliado, String grau, String nipAvaliador) {
        Avaliacao avaliacao = buscarEValidarAcesso(idAvaliacao, nipAvaliador);

        if (avaliacao.getStatus() == StatusAvaliacao.CONCLUIDA) {
            throw new IllegalStateException("Esta avaliação já foi concluída e não pode ser alterada.");
        }

        Avaliado avaliado = avaliadoRepository.findById(idAvaliado)
                .orElseThrow(() -> new RuntimeException("Avaliado não encontrado com ID: " + idAvaliado));

        if (!avaliado.getAvaliacao().getId().equals(idAvaliacao)) {
            throw new AccessDeniedException("Acesso negado. O avaliado não pertence a esta avaliação.");
        }

        try {
            String dadosJson = utilitarioCriptografia.decifrar(avaliado.getDadosCifrados());
            DadosAvaliadoDTO dadosAtuais = objectMapper.readValue(dadosJson, DadosAvaliadoDTO.class);

            DadosAvaliadoDTO dadosAtualizados = new DadosAvaliadoDTO(
                    dadosAtuais.nip(), dadosAtuais.nome(), dadosAtuais.nomeDeGuerra(), dadosAtuais.sequencia(),
                    dadosAtuais.especialidade(), dadosAtuais.antiguidade(), dadosAtuais.posto(), dadosAtuais.quadro(),
                    dadosAtuais.omSigla(), dadosAtuais.fotoBase64(), grau
            );

            String novoJsonCifrado = utilitarioCriptografia.cifrar(objectMapper.writeValueAsString(dadosAtualizados));
            avaliado.setDadosCifrados(novoJsonCifrado);

            if (avaliacao.getStatus() == StatusAvaliacao.INICIADA) {
                avaliacao.setStatus(StatusAvaliacao.EM_PROGRESSO);
            }

            avaliadoRepository.save(avaliado);
            avaliacaoRepository.save(avaliacao);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao processar dados do avaliado", e);
        }
    }

    @Override
    @Transactional
    public void enviarAvaliacao(Long idAvaliacao, String nipAvaliador) {
        Avaliacao avaliacao = buscarEValidarAcesso(idAvaliacao, nipAvaliador);

        boolean todosPreenchidos = avaliacao.getAvaliados().stream()
                .allMatch(a -> {
                    try {
                        String dadosJson = utilitarioCriptografia.decifrar(a.getDadosCifrados());
                        DadosAvaliadoDTO dados = objectMapper.readValue(dadosJson, DadosAvaliadoDTO.class);
                        return dados.grau() != null && !dados.grau().isBlank();
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("Erro ao verificar grau do avaliado", e);
                    }
                });

        if (!todosPreenchidos) {
            throw new IllegalStateException("Não é possível enviar a avaliação. Todos os graus devem ser preenchidos.");
        }

        avaliacao.setStatus(StatusAvaliacao.CONCLUIDA);
        avaliacao.setDataEnvio(OffsetDateTime.now());
        avaliacaoRepository.save(avaliacao);
    }

    private Avaliacao buscarEValidarAcesso(Long idAvaliacao, String nipAvaliador) {
        Avaliacao avaliacao = avaliacaoRepository.findById(idAvaliacao)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada com ID: " + idAvaliacao));

        if (!avaliacao.getAvaliador().getNip().equals(nipAvaliador)) {
            throw new AccessDeniedException("Acesso negado. Você não é o avaliador desta FAC.");
        }
        return avaliacao;
    }

    private AvaliacaoDetalheDTO mapearParaDetalheDTO(Avaliacao avaliacao) {
        AvaliacaoDetalheDTO.AvaliadorDTO avaliadorDTO = new AvaliacaoDetalheDTO.AvaliadorDTO(
                avaliacao.getAvaliador().getNome(),
                avaliacao.getAvaliador().getPostoAvaliador().getDescricao(),
                avaliacao.getAvaliador().getQuadroAvaliador().getDescricao(),
                avaliacao.getAvaliador().getFotoAvaliadorBase64()
        );

        List<AvaliacaoDetalheDTO.AvaliadoDTO> avaliadosDTO = avaliacao.getAvaliados().stream()
                .map(avaliado -> {
                    try {
                        String dadosJson = utilitarioCriptografia.decifrar(avaliado.getDadosCifrados());
                        DadosAvaliadoDTO dados = objectMapper.readValue(dadosJson, DadosAvaliadoDTO.class);
                        return new AvaliacaoDetalheDTO.AvaliadoDTO(
                                avaliado.getId(), dados.nip(), dados.nome(), dados.nomeDeGuerra(),
                                dados.antiguidade(), dados.omSigla(), dados.fotoBase64(), dados.grau()
                        );
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("Erro ao decifrar dados do avaliado para DTO", e);
                    }
                })
                .collect(Collectors.toList());

        return new AvaliacaoDetalheDTO(
                avaliacao.getId(),
                avaliacao.getEventoTipo().getSigla(),
                avaliacao.getEventoTipo().getDescricao(),
                avaliacao.getEventoPosto() != null ? avaliacao.getEventoPosto().getSigla() : "",
                avaliacao.getEventoQuadro() != null ? avaliacao.getEventoQuadro().name() : "",
                avaliacao.getSituacaoPromocao(),
                avaliacao.getDataLimiteRemessa(),
                avaliacao.getStatus(),
                avaliadorDTO,
                avaliadosDTO
        );
    }
}