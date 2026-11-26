package cl.duoc.backend_tienda.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frame -> frame.disable())) // Para H2 Console
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(List.of("*"));
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(List.of("*"));
                return config;
            }))
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas: login, catálogo, categorías, Swagger y consola H2
                .requestMatchers("/api/v1/auth/**", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/h2-console/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/productos/**", "/api/v1/categorias/**").permitAll()

                // Reglas por rol exigidas en pauta:
                // ADMIN: gestión total de productos
                .requestMatchers(HttpMethod.POST, "/api/v1/productos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/productos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/productos/**").hasRole("ADMIN")

                // BOLETAS: CLIENTE y ADMIN pueden crear; ADMIN y VENDEDOR pueden ver listado
                .requestMatchers(HttpMethod.POST, "/api/v1/boletas/**").hasAnyRole("CLIENTE", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/boletas/**").hasAnyRole("ADMIN", "VENDEDOR")

                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
