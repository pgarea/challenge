package es.pgomez.tc.testing.domain.model.common;

import jakarta.validation.ValidationException;

import java.time.LocalDateTime;

public record CreationDateType(LocalDateTime creationDate) {

    private static final String VALIDATION_ERR_MSG = "INVALID_CREATION_DATE";

    /**
     * Validates a creation date.
     *
     * @param creationDate
     * @return true CreationDate is before now
     * false CreationDate is invalid
     */
    public CreationDateType {
        if (!isValidCreationDate(creationDate)) {
            throw new ValidationException(VALIDATION_ERR_MSG);
        }
    }

    private boolean isValidCreationDate(LocalDateTime creationDate) {
        return creationDate != null && creationDate.isBefore(LocalDateTime.now());
    }
}
