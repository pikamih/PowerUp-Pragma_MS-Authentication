package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.request.UserRequestDto;
import co.com.pragma.api.dto.response.UserResponseDto;
import co.com.pragma.model.user.User;

/**
 * Mapper para convertir entre DTOs de usuario y entidad de dominio.
 */
public class UserWebMapper {

    /**
     * Convierte un UserRequestDto a User (dominio)
     */
    public static User toDomain(UserRequestDto dto) {
        return User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .documentId(dto.getDocumentId())
                .birthDate(dto.getBirthDate())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .baseSalary(dto.getBaseSalary())
                .roleId(dto.getRoleId())
                .build();
    }

    /**
     * Convierte un User (dominio) a UserResponseDto
     */
    public static UserResponseDto toResponse(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .documentId(user.getDocumentId())
                .birthDate(user.getBirthDate())
                .address(user.getAddress())
                .phone(user.getPhone())
                .email(user.getEmail())
                .baseSalary(user.getBaseSalary())
                .roleId(user.getRoleId())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
