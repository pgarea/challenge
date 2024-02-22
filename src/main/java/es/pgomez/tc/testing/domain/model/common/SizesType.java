package es.pgomez.tc.testing.domain.model.common;

import jakarta.validation.ValidationException;

import java.util.List;

public class SizesType {

    private static final String VALIDATION_ERR_MSG = "INVALID_SIZE_LIST";

    private final List<SizeType> sizes;

    public SizesType(List<String> sizeList) {
        if (!isValidSizeList(sizeList)) {
            throw new ValidationException(VALIDATION_ERR_MSG);
        }
        this.sizes = sizeList.stream().map(SizeType::new).toList();
    }

    public List<SizeType> sizes() {
        return sizes;
    }

    /**
     * Validates a size list.
     *
     * @param sizes
     * @return true Size is not null
     * false Size is invalid
     */
    private boolean isValidSizeList(List<String> sizes) {
        return sizes != null;
    }

}
