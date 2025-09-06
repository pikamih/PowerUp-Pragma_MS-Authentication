package co.com.pragma.model.mock;

import co.com.pragma.model.role.Role;

import java.util.UUID;

public class RoleMock {

    public static Role getDefault() {
        return Role.builder()
                .id(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .name("ADMIN")
                .description("Administrator role")
                .build();
    }

    public static Role getWithCustomName(String name) {
        return Role.builder()
                .id(UUID.fromString("22222222-2222-2222-2222-222222222222"))
                .name(name)
                .description("Custom role")
                .build();
    }

    public static Role getWithCustomId(UUID id) {
        return Role.builder()
                .id(id)
                .name("USER")
                .description("User role")
                .build();
    }
}
