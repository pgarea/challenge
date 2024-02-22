package es.pgomez.tc.testing.domain.model.common;

import jakarta.validation.ValidationException;

import java.util.regex.Pattern;


public record LogoType(String logo) {

    private static final String VALIDATION_ERR_MSG = "INVALID_LOGO_URL";

    public LogoType {
        if (!isValidUrl(logo)) {
            throw new ValidationException(VALIDATION_ERR_MSG);
        }
    }

    private boolean isValidUrl(String logo) {
        String regexPattern = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        return logo != null && !logo.isEmpty() && !logo.isBlank() && Pattern.compile(regexPattern).matcher(logo).matches();
    }


}
