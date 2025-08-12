package mb.cpo.facdigital.service.impl;

import lombok.RequiredArgsConstructor;
import mb.cpo.facdigital.dto.DadosPrimeiroAcessoDTO;
import mb.cpo.facdigital.model.entity.TokenPrimeiroAcesso;
import mb.cpo.facdigital.repository.TokenPrimeiroAcessoRepository;
import mb.cpo.facdigital.service.EmailService;
import mb.cpo.facdigital.service.PrimeiroAcessoService;
import mb.cpo.facdigital.service.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class PrimeiroAcessoServiceImpl implements PrimeiroAcessoService {

    private final TokenPrimeiroAcessoRepository tokenRepository;
    private final UsuarioService usuarioService;
    private final EmailService emailService;

    @Override
    @Transactional
    public void iniciarProcesso(String nip, String email) {

        String tokenValue = String.format("%06d", new SecureRandom().nextInt(999999));

        tokenRepository.findByNipAvaliador(nip).ifPresent(tokenRepository::delete);

        TokenPrimeiroAcesso novoToken = new TokenPrimeiroAcesso();
        novoToken.setNipAvaliador(nip);
        novoToken.setEmail(email);
        novoToken.setToken(tokenValue);
        novoToken.setDataExpiracao(OffsetDateTime.now().plusMinutes(15));
        tokenRepository.save(novoToken);

        String assunto = "Seu código de validação - Portal FACDigital";
        String texto = "Olá, avaliador.\n\nSeu código para validação de primeiro acesso é: " + tokenValue + "\n\nEste código expira em 15 minutos.";
        emailService.enviarEmail(email, assunto, texto);
    }

    @Override
    @Transactional
    public void finalizarProcesso(DadosPrimeiroAcessoDTO dadosPrimeiroAcessoDTO) {
        // Valida o token
        TokenPrimeiroAcesso token = tokenRepository.findByToken(dadosPrimeiroAcessoDTO.getToken())
                .orElseThrow(() -> new RuntimeException("Token inválido."));

        if (token.getDataExpiracao().isBefore(OffsetDateTime.now())) {
            tokenRepository.delete(token);
            throw new RuntimeException("Token expirado. Por favor, inicie o processo novamente.");
        }

        usuarioService.criarNovoUsuario(dadosPrimeiroAcessoDTO.getNip(), dadosPrimeiroAcessoDTO.getNome(), dadosPrimeiroAcessoDTO.getEmail(), dadosPrimeiroAcessoDTO.getSenha());

        tokenRepository.delete(token);

    }
}