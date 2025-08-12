package mb.cpo.facdigital.repository;

import mb.cpo.facdigital.model.entity.TokenPrimeiroAcesso;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TokenPrimeiroAcessoRepository extends JpaRepository<TokenPrimeiroAcesso, Long> {
    Optional<TokenPrimeiroAcesso> findByNipAvaliador(String nipAvaliador);
    Optional<TokenPrimeiroAcesso> findByToken(String token);
}