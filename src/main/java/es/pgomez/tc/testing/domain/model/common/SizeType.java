package es.pgomez.tc.testing.domain.model.common;

import jakarta.validation.ValidationException;

public record SizeType(String size) {

    private static final String VALIDATION_ERR_MSG = "INVALID_SIZE";

    public SizeType {
        if (!isValidName(size)) {
            throw new ValidationException(VALIDATION_ERR_MSG);
        }
    }

    /**
     * Validates a size.
     *
     * @param size
     * @return true Size is not empty and has chars different from blank
     * false Name is invalid
     */
    private boolean isValidName(String size) {
        return size != null && !size.isEmpty() && !size.isBlank();
    }
}
