package mb.cpo.facdigital.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import mb.cpo.facdigital.security.UsuarioPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Componente responsável por gerar e validar os tokens JWT.
 */
@Component
public class ProvedorTokenJwt {

    private static final Logger logger = LoggerFactory.getLogger(ProvedorTokenJwt.class);

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private int jwtExpirationMs;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Gera um token JWT usando a sintaxe moderna e não-depreciada.
     */
    public String gerarToken(Authentication authentication) {
        UsuarioPrincipal usuarioPrincipal = (UsuarioPrincipal) authentication.getPrincipal();

        Date agora = new Date();
        Date dataExpiracao = new Date(agora.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .subject(usuarioPrincipal.getUsername()) // Nova sintaxe para setSubject
                .issuedAt(agora)                         // Nova sintaxe para setIssuedAt
                .expiration(dataExpiracao)               // Nova sintaxe para setExpiration
                .signWith(getSigningKey())               // Nova sintaxe para signWith (sem o algoritmo)
                .compact();
    }

    /**
     * Extrai o NIP (subject) de um token JWT.
     * @param token O token JWT.
     * @return O NIP do usuário.
     */
    public String getNipDoToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public boolean validarToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parse(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Token JWT inválido ou expirado: {}", e.getMessage());
        }
        return false;
    }
}