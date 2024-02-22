package es.pgomez.tc.testing.domain.model.common;

import jakarta.validation.ValidationException;

public record ColorType(String color) {

    private static final String VALIDATION_ERR_MSG = "INVALID_COLOR";

    public ColorType {
        if (!isValidColor(color)) {
            throw new ValidationException(VALIDATION_ERR_MSG);
        }
    }

    /**
     * Validates a color.
     *
     * @param color
     * @return true Color is not empty and has chars different from blank
     * false Color is invalid
     */
    private boolean isValidColor(String color) {
        return color != null && !color.isEmpty() && !color.isBlank();
    }

}
