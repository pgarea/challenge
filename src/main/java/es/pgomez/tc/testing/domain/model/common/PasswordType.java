package es.pgomez.tc.testing.domain.model.common;

import es.pgomez.tc.testing.util.auth.Crypto;
import jakarta.validation.ValidationException;

import java.util.regex.Pattern;

public class PasswordType {

    private final String password;
    private static final String VALIDATION_ERR_MSG = "INVALID_PASSWORD";

    public PasswordType(String password, boolean isEncoded) {
        if (isEncoded) {
            this.password = password;
        } else {
            if (!isValidPassword(password)) {
                throw new ValidationException(VALIDATION_ERR_MSG);
            }
            this.password = Crypto.hashPassword(password);
        }
    }

    public String password() {
        return password;
    }

    /**
     * Validates a password.
     *
     * @param password
     * @return true Password is not null and has length between 8 and 20 characters and no empty spaces
     * false Password is invalid
     */
    public boolean isValidPassword(String password) {
        String regexPattern = "^(?=\\S+$).{8,20}$";
        return password != null && Pattern.compile(regexPattern).matcher(password).matches();
    }

}
