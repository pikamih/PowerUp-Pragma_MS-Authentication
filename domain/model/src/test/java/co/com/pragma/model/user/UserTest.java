package co.com.pragma.model.user;

import co.com.pragma.model.mock.UserMock;
import co.com.pragma.model.role.Role;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testBuilderAndGetters() {
        User user = UserMock.getDefault();

        assertEquals(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), user.getId());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("12345678", user.getDocumentId());
        assertEquals(LocalDate.of(1990, 1, 1), user.getBirthDate());
        assertEquals("123 Main St", user.getAddress());
        assertEquals("987654321", user.getPhone());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals(2500.0, user.getBaseSalary());
        assertEquals("ADMIN", user.getRole().getName());
        assertEquals(LocalDateTime.of(2025, 1, 1, 10, 0), user.getCreatedAt());
        assertEquals(LocalDateTime.of(2025, 1, 2, 10, 0), user.getUpdatedAt());
    }

    @Test
    void testSetters() {
        User user = new User();
        UUID id = UUID.randomUUID();
        user.setId(id);
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setDocumentId("87654321");
        user.setBirthDate(LocalDate.of(1995, 5, 15));
        user.setAddress("456 Second St");
        user.setPhone("912345678");
        user.setEmail("jane.smith@example.com");
        user.setPassword("pass456");
        user.setBaseSalary(3000.0);
        Role role = Role.builder().id(UUID.randomUUID()).name("USER").description("User role").build();
        user.setRole(role);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        assertEquals("Jane", user.getFirstName());
        assertEquals("Smith", user.getLastName());
        assertEquals("87654321", user.getDocumentId());
        assertEquals("456 Second St", user.getAddress());
        assertEquals("912345678", user.getPhone());
        assertEquals("jane.smith@example.com", user.getEmail());
        assertEquals("pass456", user.getPassword());
        assertEquals(3000.0, user.getBaseSalary());
        assertEquals("USER", user.getRole().getName());
    }

    @Test
    void testToBuilder() {
        User user = UserMock.getDefault();

        User modified = user.toBuilder()
                .email("modified@example.com")
                .build();

        assertEquals("modified@example.com", modified.getEmail());
        assertEquals("John", modified.getFirstName());
        assertEquals("Doe", modified.getLastName());
        assertEquals("ADMIN", modified.getRole().getName());
    }

    @Test
    void testEqualsAndHashCode() {
        User u1 = UserMock.getDefault();
        User u2 = UserMock.getDefault();
        User u3 = UserMock.getWithCustomEmail("other@example.com");

        assertEquals(u1, u2);
        assertNotEquals(u1, u3);
        assertEquals(u1.hashCode(), u2.hashCode());
        assertNotEquals(u1.hashCode(), u3.hashCode());
    }
}
