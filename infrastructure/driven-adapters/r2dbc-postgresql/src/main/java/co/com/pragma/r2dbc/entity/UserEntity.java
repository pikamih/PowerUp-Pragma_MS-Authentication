package co.com.pragma.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("users")
public class UserEntity {

    @Id
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
    private UUID roleId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
