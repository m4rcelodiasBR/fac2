package mb.cpo.facdigital.service;

import mb.cpo.facdigital.dto.avaliacao.AvaliacaoDetalheDTO;
import mb.cpo.facdigital.dto.avaliacao.AvaliacaoResumoDTO;
import mb.cpo.facdigital.dto.avaliacao.DadosXmlDTO;
import mb.cpo.facdigital.model.entity.Avaliacao;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AvaliacaoService {

    Avaliacao criarNovaAvaliacaoAPartirDeXml(MultipartFile arquivo, String nipAvaliador);

    List<AvaliacaoResumoDTO> listarAvaliacoesPorAvaliador(String nipAvaliador);

    AvaliacaoDetalheDTO buscarAvaliacaoDetalhada(Long idAvaliacao, String nipAvaliador);

    void atualizarGrau(Long idAvaliacao, Long idAvaliado, String grau, String nipAvaliador);

    void enviarAvaliacao(Long idAvaliacao, String nipAvaliador);

}