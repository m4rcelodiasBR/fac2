package mb.cpo.facdigital.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mb.cpo.facdigital.security.DetalhesUsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro que intercepta todas as requisições para validar o token JWT
 * presente no header 'Authorization'.
 */
@Component
public class FiltroAutenticacaoJwt extends OncePerRequestFilter {

    @Autowired
    private ProvedorTokenJwt provedorTokenJwt;

    @Autowired
    private DetalhesUsuarioServiceImpl detalhesUsuarioService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = extrairTokenDoRequest(request);

            if (jwt != null && provedorTokenJwt.validarToken(jwt)) {
                String nip = provedorTokenJwt.getNipDoToken(jwt);

                UserDetails userDetails = detalhesUsuarioService.loadUserByUsername(nip);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Seta o usuário no contexto de segurança do Spring.
                // A partir daqui, o usuário está "autenticado" para esta requisição.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Não foi possível setar a autenticação do usuário: {}");
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extrai o token do header 'Authorization: Bearer <token>'.
     * @param request A requisição HTTP.
     * @return O token JWT ou null se não encontrado.
     */
    private String extrairTokenDoRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}