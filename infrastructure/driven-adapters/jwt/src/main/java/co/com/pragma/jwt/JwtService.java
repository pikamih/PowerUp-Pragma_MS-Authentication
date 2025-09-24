package co.com.pragma.jwt;

import co.com.pragma.jwt.adapter.PemKeys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

@Service
public class JwtService {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final long expirationMillis ; // 1 hora

    public JwtService(
            @Value("${security.jwt.private-key-path}") String privateKey,
            @Value("${security.jwt.public-key-path}") String publicKey,
            @Value("${security.jwt.expiration}") long expirationSeconds
    ) throws Exception {
        this.privateKey = PemKeys.readPrivateKeyFromString(privateKey);
        this.publicKey = PemKeys.readPublicKeyFromString(publicKey);
        this.expirationMillis = expirationSeconds * 1000;
    }

    public Mono<TokenWithExpiry> generateToken(String username, String role, String documentId) {
        return Mono.fromSupplier(() -> {

            long now = System.currentTimeMillis();
            long expiry = now + expirationMillis;
            String token = Jwts.builder()
                    .setSubject(username)
                    .claim("role", role)
                    .claim("documentId", documentId)
                    .setIssuedAt(new Date(now))
                    .setExpiration(new Date(expiry))
                    .signWith(privateKey, SignatureAlgorithm.RS256)
                    .compact();
            return new TokenWithExpiry(token, expiry);

        });
    }

    public Mono<Claims> validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return Mono.just(claims);
        } catch (JwtException e) {
            return Mono.error(e);
        }
    }
}
