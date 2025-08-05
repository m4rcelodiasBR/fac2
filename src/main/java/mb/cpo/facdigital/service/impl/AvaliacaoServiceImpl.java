package mb.cpo.facdigital.service.impl;

import mb.cpo.facdigital.model.entity.Avaliacao;
import mb.cpo.facdigital.model.entity.Usuario;
import mb.cpo.facdigital.repository.AvaliacaoRepository;
import mb.cpo.facdigital.repository.UsuarioRepository;
import mb.cpo.facdigital.service.AvaliacaoService;
import mb.cpo.facdigital.service.ProcessamentoArquivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AvaliacaoServiceImpl implements AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProcessamentoArquivoService processamentoArquivoService;

    @Override
    @Transactional // Garante que todas as operações de banco sejam feitas em uma única transação
    public Avaliacao criarNovaAvaliacaoAPartirDeXml(MultipartFile arquivo, String nipAvaliador) {
        // 1. Busca o usuário avaliador no banco de dados.
        Usuario avaliador = usuarioRepository.findByNip(nipAvaliador)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + nipAvaliador));

        // 2. Delega o processamento do arquivo para o serviço especializado.
        Avaliacao novaAvaliacao = processamentoArquivoService.processarArquivoXml(arquivo, avaliador);

        // 3. Salva a nova avaliação e seus avaliados (em cascata) no banco de dados.
        return avaliacaoRepository.save(novaAvaliacao);
    }
}