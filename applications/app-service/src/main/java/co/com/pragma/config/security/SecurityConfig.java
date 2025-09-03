package co.com.pragma.config.security;

import co.com.pragma.jwt.adapter.JwtAuthenticationWebFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationWebFilter jwtAuthenticationWebFilter;

    public SecurityConfig(JwtAuthenticationWebFilter jwtAuthenticationWebFilter) {
        this.jwtAuthenticationWebFilter = jwtAuthenticationWebFilter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                        .pathMatchers("/api/v1/users/**").hasAnyRole("ADMIN", "ASESOR")
                        .pathMatchers("/api/v1/roles/**").hasRole("ADMIN")
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtAuthenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}
