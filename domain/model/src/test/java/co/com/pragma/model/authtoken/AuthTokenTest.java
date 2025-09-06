package co.com.pragma.model.authtoken;

import co.com.pragma.model.mock.AuthTokenMock;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class AuthTokenTest {

    @Test
    void testBuilderAndGetters() {
        AuthToken token = AuthTokenMock.getDefault();

        assertEquals("John Doe", token.getName());
        assertEquals("john@example.com", token.getEmail());
        assertEquals("12345678", token.getDocumentId());
        assertEquals("ADMIN", token.getRole());
        assertEquals("jwt-token-xyz", token.getToken());
        assertEquals("2025-12-31T23:59:59Z", token.getExpiresAt());
    }

    @Test
    void testSetters() {
        String dateString = "2025-11-30T12:00:00Z"; // tu fecha en formato ISO
        long timestamp = Instant.parse(dateString).toEpochMilli();

        AuthToken token = new AuthToken();
        token.setName("Jane Doe");
        token.setEmail("jane@example.com");
        token.setDocumentId("87654321");
        token.setRole("USER");
        token.setToken("jwt-token-abc");
        token.setExpiresAt("2025-11-30T12:00:00Z");

        assertEquals("Jane Doe", token.getName());
        assertEquals("jane@example.com", token.getEmail());
        assertEquals("87654321", token.getDocumentId());
        assertEquals("USER", token.getRole());
        assertEquals("jwt-token-abc", token.getToken());
        assertEquals("2025-11-30T12:00:00Z", token.getExpiresAt());
    }

    @Test
    void testToBuilder() {
        AuthToken token = AuthTokenMock.getDefault();

        AuthToken modified = token.toBuilder()
                .token("modified-token")
                .build();

        assertEquals("John Doe", modified.getName());
        assertEquals("john@example.com", modified.getEmail());
        assertEquals("12345678", modified.getDocumentId());
        assertEquals("ADMIN", modified.getRole());
        assertEquals("modified-token", modified.getToken());
        assertEquals("2025-12-31T23:59:59Z", modified.getExpiresAt());
    }

    @Test
    void testEqualsAndHashCode() {
        AuthToken t1 = AuthTokenMock.getDefault();
        AuthToken t2 = AuthTokenMock.getDefault();
        AuthToken t3 = AuthTokenMock.getWithCustomName("User2");

        assertEquals(t1, t2);
        assertNotEquals(t1, t3);
        assertEquals(t1.hashCode(), t2.hashCode());
        assertNotEquals(t1.hashCode(), t3.hashCode());
    }
}
