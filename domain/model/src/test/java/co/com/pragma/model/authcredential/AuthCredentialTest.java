package co.com.pragma.model.authcredential;

import co.com.pragma.model.mock.AuthCredentialMock;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AuthCredentialTest {

    @Test
    void testBuilderAndGetters() {
        AuthCredential auth = AuthCredentialMock.getDefault();

        assertEquals("user@example.com", auth.getEmail());
        assertEquals("password123", auth.getPassword());
    }

    @Test
    void testSetters() {
        AuthCredential auth = new AuthCredential();
        auth.setEmail("test@example.com");
        auth.setPassword("pass456");

        assertEquals("test@example.com", auth.getEmail());
        assertEquals("pass456", auth.getPassword());
    }

    @Test
    void testToBuilder() {
        AuthCredential auth = AuthCredentialMock.getDefault();

        AuthCredential modified = auth.toBuilder()
                .password("newPass")
                .build();

        assertEquals("user@example.com", modified.getEmail());
        assertEquals("newPass", modified.getPassword());
    }

    @Test
    void testEqualsAndHashCode() {
        AuthCredential a1 = AuthCredentialMock.getDefault();
        AuthCredential a2 = AuthCredentialMock.getDefault();
        AuthCredential a3 = AuthCredentialMock.getWithCustomEmail("b@example.com");

        assertEquals(a1, a2);
        assertNotEquals(a1, a3);

        assertEquals(a1.hashCode(), a2.hashCode());
        assertNotEquals(a1.hashCode(), a3.hashCode());
    }
}
