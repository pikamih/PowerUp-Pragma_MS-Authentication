package co.com.pragma.r2dbc.mapper;

import co.com.pragma.model.role.Role;
import co.com.pragma.model.user.User;
import co.com.pragma.r2dbc.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {

    @Mapping(target = "role", source = "roleId")
    User toDomain(UserEntity entity);

    @Mapping(target = "roleId", source = "role")
    UserEntity toEntity(User user);

    default Role map(UUID roleId) {
        if (roleId == null) return null;
        Role role = new Role();
        role.setId(roleId);
        return role;
    }

    default UUID map(Role role) {
        return role != null ? role.getId() : null;
    }
}

