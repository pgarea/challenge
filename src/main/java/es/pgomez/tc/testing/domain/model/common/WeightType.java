package es.pgomez.tc.testing.domain.model.common;

import jakarta.validation.ValidationException;

public record WeightType(Double weight) {

    private static final String VALIDATION_ERR_MSG = "INVALID_WEIGHT";

    public WeightType(Double weight) {
        if (!isValidWeight(weight)) {
            throw new ValidationException(VALIDATION_ERR_MSG);
        }
        this.weight = Math.round(weight * 10) / 10d;
    }

    /**
     * Validates a weight.
     *
     * @param weight
     * @return true Weight is positive
     * false Color is invalid
     */
    private boolean isValidWeight(Double weight) {
        return weight != null && weight > 0;
    }
}
