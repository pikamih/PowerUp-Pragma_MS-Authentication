package co.com.pragma.usecase.common.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageCode {
    // ===== Required fields =====
    USER_FIRST_NAME_REQUIRED("u0001", "First name is required.", 400),
    USER_LAST_NAME_REQUIRED("u0002", "Last name is required.", 400),
    USER_EMAIL_REQUIRED("u0003", "Email is required.", 400),
    USER_DOCUMENT_ID_REQUIRED("u0004", "Document ID is required.", 400),
    USER_BASE_SALARY_REQUIRED("u0005", "Base salary is required.", 400),

    ROLE_NAME_REQUIRED("r0001", "Role name is required.", 400),

    // ===== Format / range validations =====
    USER_BASE_SALARY_INVALID("u0006", "Base salary must be between 0 and 15,000,000.", 400),
    USER_EMAIL_INVALID("u0007", "Invalid email format.", 400),
    USER_ROLE_REQUIRED("u0008", "Role is required for the user.", 400),
    USER_INVALID_PASSWORD("u0009", "Incorrect password.", 400),
    USER_PASSWORD_REQUIRED("u0010", "Password is required.", 400),
    USER_DOCUMENT_ID_CANNOT_BE_CHANGED("u0011", "Document ID cannot be changed.", 400),

    // ===== Uniqueness conflicts =====
    USER_EMAIL_ALREADY_EXISTS("u0012", "The email {0} is already registered.", 409),
    USER_DOCUMENT_ID_ALREADY_EXISTS("u0013", "The document ID is already registered.", 409),
    ROLE_ALREADY_EXISTS("r0002", "Role name already exists.", 409),

    // ===== Resource not found =====
    USER_NOT_FOUND_BY_ID("u0014", "User with ID {0} does not exist.", 404),
    ROLE_NOT_FOUND_BY_ID("r0003", "Role with ID {0} does not exist.", 404),
    USER_ROLE_NOT_FOUND("u0015", "Role not found.", 404),
    USER_NOT_FOUND_BY_EMAIL("u0016", "Email {0} does not exist.", 404),

    // ===== Unauthorized =====
    USER_NOT_AUTHORIZED("u0017", "User not authorized.", 401);

    private final String errorCode;
    private final String message;
    private final Integer httpStatus;
}
