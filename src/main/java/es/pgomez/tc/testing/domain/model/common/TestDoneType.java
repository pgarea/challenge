package es.pgomez.tc.testing.domain.model.common;

import jakarta.validation.ValidationException;

public record TestDoneType(Long testDone) {

    private static final String VALIDATION_ERR_MSG = "INVALID_TEST_DONE";

    public TestDoneType {
        if (!isValidTestDone(testDone)) {
            throw new ValidationException(VALIDATION_ERR_MSG);
        }
    }

    /**
     * Validates the number of test done.
     *
     * @param TestDone
     * @return true TestDone is positive
     * false Color is invalid
     */
    private boolean isValidTestDone(Long testDone) {
        return testDone != null && testDone >= 0;
    }
}
