package es.pgomez.tc.testing.domain.model.common;

import jakarta.validation.ValidationException;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public record PicturesType(List<String> pictures) {

    private static final String VALIDATION_ERR_MSG = "INVALID_PICTURE_LIST";

    public PicturesType {
        if (!isValidPictures(pictures)) {
            throw new ValidationException(VALIDATION_ERR_MSG);
        }
    }

    private boolean isValidPictures(List<String> urls) {
        if (urls == null) {
            return false;
        }

        String regex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern regexPattern = Pattern.compile(regex);
        Optional<Boolean> isValid = urls.stream().map(url -> regexPattern.matcher(url).matches()).reduce((x, y) -> x && y);
        return urls.isEmpty() || Boolean.TRUE.equals(isValid.get());
    }
}
