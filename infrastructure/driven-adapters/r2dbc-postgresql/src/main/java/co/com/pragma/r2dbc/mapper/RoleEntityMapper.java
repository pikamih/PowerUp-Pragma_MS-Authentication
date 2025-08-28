package co.com.pragma.r2dbc.mapper;

import co.com.pragma.model.role.Role;
import co.com.pragma.r2dbc.entity.RoleEntity;

public class RoleEntityMapper {

    private RoleEntityMapper() {
        // Evitamos instanciación
    }

    public static Role toRole(RoleEntity entity) {
        if (entity == null) return null;
        return new Role(entity.getId(), entity.getName(), entity.getDescription());
    }

    public static RoleEntity fromRole(Role role) {
        if (role == null) return null;
        return new RoleEntity(role.getId(), role.getName(), role.getDescription());
    }
}
