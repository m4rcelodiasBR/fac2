package mb.cpo.facdigital.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SegurancaConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        // 1. Permite acesso público a recursos estáticos, à home e ao login
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/login").permitAll()
                        // 2. NOVA REGRA: Permite acesso público a todo o fluxo de primeiro acesso
                        .requestMatchers("/primeiro-acesso/**").permitAll()
                        // 3. NOVA REGRA: Permite acesso público ESPECIFICAMENTE ao endpoint de upload
                        .requestMatchers("/fac/carregar-xml").permitAll()
                        // 4. Protege o painel do administrador
                        .requestMatchers("/dashboard/**").hasRole("ADMIN")
                        // 5. Protege o restante da área do avaliador
                        .requestMatchers("/fac/**").hasRole("AVALIADOR")
                        // 6. Exige autenticação para qualquer outra requisição
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/fac/painel", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll());

        return http.build();
    }
}