package co.com.pragma.api.dto.response;

import co.com.pragma.model.role.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO de respuesta para datos de usuario.
 */
@Data
@Builder
public class UserResponseDto {

    private UUID id;
    private String firstName;
    private String lastName;
    private String documentId;
    private LocalDate birthDate;
    private String address;
    private String phone;
    private String email;
    private Double baseSalary;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
