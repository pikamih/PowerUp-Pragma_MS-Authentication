package co.com.pragma.usecase.mock;

import co.com.pragma.model.role.Role;
import co.com.pragma.model.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class UserMock {

    public static User getDefault() {
        Role role = Role.builder()
                .id(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .name("ADMIN")
                .description("Administrator role")
                .build();

        return User.builder()
                .id(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                .firstName("John")
                .lastName("Doe")
                .documentId("12345678")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .phone("987654321")
                .email("john.doe@example.com")
                .password("password123")
                .baseSalary(2500.0)
                .role(role)
                .createdAt(LocalDateTime.of(2025, 1, 1, 10, 0))
                .updatedAt(LocalDateTime.of(2025, 1, 2, 10, 0))
                .build();
    }

    public static User getWithCustomEmail(String email) {
        User user = getDefault();
        user.setEmail(email);
        return user;
    }

    public static User getWithCustomRole(Role role) {
        User user = getDefault();
        user.setRole(role);
        return user;
    }
}
