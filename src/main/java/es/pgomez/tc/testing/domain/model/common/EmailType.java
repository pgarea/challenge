package es.pgomez.tc.testing.domain.model.common;

import jakarta.validation.ValidationException;

import java.util.regex.Pattern;

public record EmailType(String email) {

    private static final String VALIDATION_ERR_MSG = "INVALID_EMAIL_ADDRESS";

    public EmailType {
        if (!isValidEmail(email)) {
            throw new ValidationException(VALIDATION_ERR_MSG);
        }
    }

    /**
     * Validates an email based on OWASP Validation Regex
     * https://owasp.org/www-community/OWASP_Validation_Regex_Repository
     *
     * @param email
     * @return true Email is valid
     * false Email is invalid
     */
    private boolean isValidEmail(String emailAddress) {
        String regexPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return emailAddress != null && Pattern.compile(regexPattern).matcher(emailAddress)
                .matches();
    }
}
