package es.pgomez.tc.testing.domain.model.common;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
class SizeTypeTest {

    @Test
    void nullSizeTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new SizeType(null));
    }

    @Test
    void emptySizeTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new SizeType(""));
    }

    @Test
    void blankSizeTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new SizeType("  "));
    }

    @Test
    void sizeTest() {
        SizeType sizeType = new SizeType("M");
        Assertions.assertEquals("M", sizeType.size());
    }
}
