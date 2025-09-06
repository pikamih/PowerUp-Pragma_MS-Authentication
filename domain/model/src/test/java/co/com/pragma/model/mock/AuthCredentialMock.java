package co.com.pragma.model.mock;

import co.com.pragma.model.authcredential.AuthCredential;

public class AuthCredentialMock {

    public static AuthCredential getDefault() {
        return AuthCredential.builder()
                .email("user@example.com")
                .password("password123")
                .build();
    }

    public static AuthCredential getWithCustomEmail(String email) {
        return AuthCredential.builder()
                .email(email)
                .password("password123")
                .build();
    }

    public static AuthCredential getWithCustomPassword(String password) {
        return AuthCredential.builder()
                .email("user@example.com")
                .password(password)
                .build();
    }
}
