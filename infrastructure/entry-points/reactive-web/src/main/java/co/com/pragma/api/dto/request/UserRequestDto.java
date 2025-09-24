package co.com.pragma.api.dto.request;

import co.com.pragma.model.role.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para entrada de datos de usuario (crear/actualizar).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDto {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no debe exceder 100 caracteres")
    private String firstName;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(max = 100, message = "El apellido no debe exceder 100 caracteres")
    private String lastName;

    @NotBlank(message = "El documento no puede estar vacío")
    @Size(max = 50, message = "El documento no debe exceder 50 caracteres")
    private String documentId;

    private LocalDate birthDate;

    @Size(max = 255, message = "La dirección no debe exceder 255 caracteres")
    private String address;

    @Size(max = 20, message = "El teléfono no debe exceder 20 caracteres")
    private String phone;

    @NotBlank(message = "El correo electrónico no puede estar vacío")
    @Email(message = "Formato de correo inválido")
    @Size(max = 150, message = "El correo no debe exceder 150 caracteres")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, max = 32, message = "La contraseña debe tener entre 8 y 32 caracteres")
    private String password;


    @NotNull(message = "El salario base es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El salario debe ser mayor o igual a 0")
    @DecimalMax(value = "15000000.0", inclusive = true, message = "El salario no debe exceder 15,000,000")
    private Double baseSalary;

    @NotNull(message = "El rol es obligatorio")
    private Role role;
}
