package co.com.pragma.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long expirationMillis = 1000 * 60 * 60; // 1 hora

    public Mono<String> generateToken(String username, String role, String documentId) {
        return Mono.fromSupplier(() ->
                Jwts.builder()
                        .setSubject(username)
                        .claim("role", role)
                        .claim("documentId", documentId)
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                        .signWith(key)
                        .compact()
        );
    }

    public Mono<Claims> validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return Mono.just(claims);
        } catch (JwtException e) {
            return Mono.error(e);
        }
    }
}
