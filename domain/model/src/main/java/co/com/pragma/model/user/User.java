package co.com.pragma.model.user;

import co.com.pragma.model.role.Role;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {

    private UUID id;
    private String firstName;
    private String lastName;
    private String documentId;
    private LocalDate birthDate;
    private String address;
    private String phone;
    private String email;
    private String password;
    private Double baseSalary;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
