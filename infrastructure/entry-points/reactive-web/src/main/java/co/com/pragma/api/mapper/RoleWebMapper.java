package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.request.RoleRequestDto;
import co.com.pragma.api.dto.response.RoleResponseDto;
import co.com.pragma.model.role.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;


@Component
@Mapper(componentModel = "spring")
public interface RoleWebMapper {

    @Mapping(target = "id", ignore = true)
    Role toDomain(RoleRequestDto dto);

    RoleResponseDto toResponse(Role role);

}
