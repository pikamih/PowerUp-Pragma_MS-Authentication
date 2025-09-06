package co.com.pragma.usecase.common.messages;

public enum MessageCode {
// ===== Campos requeridos =====
    USER_FIRST_NAME_REQUIRED,
    USER_LAST_NAME_REQUIRED,
    USER_EMAIL_REQUIRED,
    USER_DOCUMENT_ID_REQUIRED,
    USER_BASE_SALARY_REQUIRED,

// ===== Validaciones de formato / rango =====
    USER_BASE_SALARY_INVALID,
    USER_EMAIL_INVALID,

// ===== Conflictos de unicidad =====
    USER_EMAIL_ALREADY_EXISTS,
    USER_DOCUMENT_ID_ALREADY_EXISTS,

// ===== Recurso no encontrado =====
    USER_NOT_FOUND_BY_ID,

    ROLE_NAME_REQUIRED,
    ROLE_ALREADY_EXISTS,
    ROLE_NOT_FOUND_BY_ID,

    USER_PASSWORD_REQUIRED,
    USER_NOT_FOUND_BY_EMAIL,
    USER_INVALID_PASSWORD,
    USER_ROLE_REQUIRED,
    USER_ROLE_NOT_FOUND,
    USER_NOT_AUTHORIZED
}
