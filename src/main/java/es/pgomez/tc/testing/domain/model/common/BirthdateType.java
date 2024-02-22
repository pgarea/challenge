package es.pgomez.tc.testing.domain.model.common;

import jakarta.validation.ValidationException;

import java.time.LocalDate;

public record BirthdateType(LocalDate birthdate) {

    private static final String VALIDATION_ERR_MSG = "INVALID_BIRTHDATE";

    public BirthdateType {
        if (!isValidBirthdate(birthdate)) {
            throw new ValidationException(VALIDATION_ERR_MSG);
        }
    }

    /**
     * Validates a birthdate.
     *
     * @param birthdate
     * @return true Birthdate is before now
     * false Birthdate is invalid
     */
    private boolean isValidBirthdate(LocalDate birthdate) {
        return birthdate != null && birthdate.isBefore(LocalDate.now());
    }

}
