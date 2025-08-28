package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.request.RoleRequestDto;
import co.com.pragma.api.dto.response.RoleResponseDto;
import co.com.pragma.model.role.Role;

import java.util.List;
import java.util.stream.Collectors;

public class RoleWebMapper {

    public static Role toDomain(RoleRequestDto dto) {
        if (dto == null) return null;
        return Role.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

    public static RoleResponseDto toResponse(Role role) {
        if (role == null) return null;
        return RoleResponseDto.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .build();
    }

    public static List<RoleResponseDto> toResponseList(List<Role> roles) {
        if (roles == null) return List.of();
        return roles.stream()
                .map(RoleWebMapper::toResponse)
                .collect(Collectors.toList());
    }
}
