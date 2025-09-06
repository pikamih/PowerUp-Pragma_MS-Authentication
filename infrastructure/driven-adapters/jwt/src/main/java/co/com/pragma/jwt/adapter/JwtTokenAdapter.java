package co.com.pragma.jwt.adapter;

import co.com.pragma.jwt.JwtService;
import co.com.pragma.jwt.TokenWithExpiry;
import co.com.pragma.model.authtoken.AuthToken;
import co.com.pragma.model.authtoken.gateways.AuthTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtTokenAdapter implements AuthTokenRepository {

    private final JwtService jwtService;

    @Override
    public Mono<AuthToken> generateToken(AuthToken authToken) {
        return jwtService.generateToken(
                        authToken.getEmail(),
                        authToken.getRole(),
                        authToken.getDocumentId())
                .map((TokenWithExpiry t) -> {
                    authToken.setToken(t.token());
                    authToken.setExpiresAt(String.valueOf(t.expiresAt()));
                    return authToken;
                });
    }

    @Override
    public Mono<AuthToken> validateToken(String token) {
        return jwtService.validateToken(token)
                .map(claims -> AuthToken.builder()
                        .email(claims.getSubject())
                        .role((String) claims.get("role"))
                        .name((String) claims.get("name")) // si quieres almacenar el nombre
                        .token(token)
                        .build()
                );
    }
}
