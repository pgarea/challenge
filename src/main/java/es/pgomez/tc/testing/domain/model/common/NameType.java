package es.pgomez.tc.testing.domain.model.common;

import jakarta.validation.ValidationException;

public record NameType(String name) {

    private static final String VALIDATION_ERR_MSG = "INVALID_NAME";

    public NameType {
        if (!isValidName(name)) {
            throw new ValidationException(VALIDATION_ERR_MSG);
        }
    }

    /**
     * Validates a name.
     *
     * @param name
     * @return true Name is not empty and has chars different from blank
     * false Name is invalid
     */
    private boolean isValidName(String name) {
        return name != null && !name.isEmpty() && !name.isBlank();
    }
}
