package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.request.UserRequestDto;
import co.com.pragma.api.dto.response.UserResponseDto;
import co.com.pragma.model.role.Role;
import co.com.pragma.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserWebMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "role", source = "role") // Aquí mapeamos UUID a Role
    User toDomain(UserRequestDto dto);

    // Aquí mapeamos Role a UUID
    UserResponseDto toResponse(User user);

}
