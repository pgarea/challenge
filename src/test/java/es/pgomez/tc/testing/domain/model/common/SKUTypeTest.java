package es.pgomez.tc.testing.domain.model.common;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
class SKUTypeTest {

    @Test
    void nullSKUTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new SKUType(null));
        ;
    }

    @Test
    void emptySKUTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new SKUType(""));
    }

    @Test
    void blankSKUTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new SKUType("  "));
    }

    @Test
    void skuTest() {
        SKUType skuType = new SKUType("Test");
        Assertions.assertEquals("Test", skuType.sku());
    }
}
