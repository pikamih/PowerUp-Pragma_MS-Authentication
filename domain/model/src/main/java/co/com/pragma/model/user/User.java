package co.com.pragma.model.user;

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
    private String firstName;     // first_name NOT NULL
    private String lastName;      // last_name NOT NULL
    private String documentId;    // document_id NOT NULL
    private LocalDate birthDate;  // birth_date
    private String address;       // address
    private String phone;         // phone
    private String email;         // email NOT NULL UNIQUE
    private Double baseSalary;    // base_salary NUMERIC(15,2) NOT NULL CHECK
    private UUID roleId;          // role_id NOT NULL, FK a roles
    private LocalDateTime createdAt; // created_at
    private LocalDateTime updatedAt; // updated_at

}
