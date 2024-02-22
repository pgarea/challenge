package es.pgomez.tc.testing.domain.model.common;

import jakarta.validation.ValidationException;

public record HeightType(Double height) {

    private static final String VALIDATION_ERR_MSG = "INVALID_HEIGHT";

    public HeightType(Double height) {
        if (!isValidHeight(height)) {
            throw new ValidationException(VALIDATION_ERR_MSG);
        }
        this.height = Math.round(height * 10) / 10d;
    }

    /**
     * Validates a height.
     *
     * @param height
     * @return true Height is positive
     * false Color is invalid
     */
    private boolean isValidHeight(Double height) {
        return height != null && height > 0;
    }
}
