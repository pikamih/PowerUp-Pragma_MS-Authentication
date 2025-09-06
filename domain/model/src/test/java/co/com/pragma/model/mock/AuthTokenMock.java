package co.com.pragma.model.mock;

import co.com.pragma.model.authtoken.AuthToken;

public class AuthTokenMock {

    public static AuthToken getDefault() {
        return AuthToken.builder()
                .name("John Doe")
                .email("john@example.com")
                .documentId("12345678")
                .role("ADMIN")
                .token("jwt-token-xyz")
                .expiresAt("2025-12-31T23:59:59Z")
                .build();
    }

    public static AuthToken getWithCustomName(String name) {
        return AuthToken.builder()
                .name(name)
                .email("john@example.com")
                .documentId("12345678")
                .role("ADMIN")
                .token("jwt-token-xyz")
                .expiresAt("2025-12-31T23:59:59Z")
                .build();
    }

    public static AuthToken getWithCustomToken(String token) {
        return AuthToken.builder()
                .name("John Doe")
                .email("john@example.com")
                .documentId("12345678")
                .role("ADMIN")
                .token(token)
                .expiresAt("2025-12-31T23:59:59Z")
                .build();
    }
}
