package es.pgomez.tc.testing.domain.model.common;

import es.pgomez.tc.testing.domain.model.common.util.Sex;
import jakarta.validation.ValidationException;

public record SexType(Sex sex) {

    private static final String VALIDATION_ERR_MSG = "INVALID_SEX";

    public SexType {
        if (!isValidSex(sex)) {
            throw new ValidationException(VALIDATION_ERR_MSG);
        }
    }

    /**
     * Validates sex.
     *
     * @param sex
     * @return true Sex is not null
     * false Sex is invalid
     */
    private boolean isValidSex(Sex sex) {
        return sex != null;
    }
}