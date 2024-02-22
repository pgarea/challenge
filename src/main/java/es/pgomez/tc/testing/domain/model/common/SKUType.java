package es.pgomez.tc.testing.domain.model.common;

import jakarta.validation.ValidationException;

public record SKUType(String sku) {

    private static final String VALIDATION_ERR_MSG = "INVALID_SKU";

    public SKUType {
        if (!isValidSKU(sku)) {
            throw new ValidationException(VALIDATION_ERR_MSG);
        }
    }

    /**
     * Validates a sku.
     *
     * @param sku
     * @return true SKU is not empty and has chars different from blank
     * false SKU is invalid
     */
    private boolean isValidSKU(String sku) {
        return sku != null && !sku.isEmpty() && !sku.isBlank();
    }

}
