package co.com.pragma.model.role;

import co.com.pragma.model.mock.RoleMock;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @Test
    void testBuilderAndGetters() {
        Role role = RoleMock.getDefault();

        assertEquals(UUID.fromString("11111111-1111-1111-1111-111111111111"), role.getId());
        assertEquals("ADMIN", role.getName());
        assertEquals("Administrator role", role.getDescription());
    }

    @Test
    void testSetters() {
        Role role = new Role();
        UUID id = UUID.randomUUID();
        role.setId(id);
        role.setName("USER");
        role.setDescription("Regular user role");

        assertEquals(id, role.getId());
        assertEquals("USER", role.getName());
        assertEquals("Regular user role", role.getDescription());
    }

    @Test
    void testToBuilder() {
        Role role = RoleMock.getDefault();

        Role modified = role.toBuilder()
                .name("MODIFIED")
                .build();

        assertEquals(UUID.fromString("11111111-1111-1111-1111-111111111111"), modified.getId());
        assertEquals("MODIFIED", modified.getName());
        assertEquals("Administrator role", modified.getDescription());
    }

    @Test
    void testEqualsAndHashCode() {
        Role r1 = RoleMock.getDefault();
        Role r2 = RoleMock.getDefault();
        Role r3 = RoleMock.getWithCustomName("USER");

        assertEquals(r1, r2);
        assertNotEquals(r1, r3);
        assertEquals(r1.hashCode(), r2.hashCode());
        assertNotEquals(r1.hashCode(), r3.hashCode());
    }
}
