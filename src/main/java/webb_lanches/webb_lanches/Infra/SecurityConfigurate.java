package webb_lanches.webb_lanches.Infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity 
public class SecurityConfigurate {
    
    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> 
            authz
                .requestMatchers(HttpMethod.POST, "/usuarios/cadastrar").permitAll() //para permitir seguir a req sem token
                .requestMatchers(HttpMethod.POST, "/usuarios/login").permitAll()
                .requestMatchers(HttpMethod.GET, 
                   "/swagger-ui/**",    // Acesso ao Swagger UI
                    "/v3/api-docs/**",   // Acesso à documentação da API
                    "/swagger-ui.html",  // Acesso à página principal do Swagger
                    "/swagger-ui/index.html" // Acesso ao Swagger UI
                ).permitAll() //para permitir seguir a req sem token
                .anyRequest().authenticated()) //todas as outras precisan do token
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //retorna o PasswordEncoder necessario para a aplicação não falhar 
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        //retorna o AuthenticationManager necessario para a aplicação não falhar 
        return configuration.getAuthenticationManager();
    }
}
