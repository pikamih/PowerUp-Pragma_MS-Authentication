package co.com.pragma.jwt.adapter;

import co.com.pragma.jwt.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
public class JwtAuthenticationWebFilter implements WebFilter {

    private final JwtService jwtService;

    public JwtAuthenticationWebFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.web.server.WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            return jwtService.validateToken(token) // devuelve Mono<Claims>
                    .flatMap(claims -> {
                        var auth = new UsernamePasswordAuthenticationToken(
                                claims.getSubject(), // ahora sí puedes acceder
                                null,
                                Collections.singleton(() -> "ROLE_" + claims.get("role"))
                        );
                        return chain.filter(exchange)
                                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
                    })
                    .onErrorResume(e -> { // captura errores de validación
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    });
        }

        return chain.filter(exchange);
    }
}
