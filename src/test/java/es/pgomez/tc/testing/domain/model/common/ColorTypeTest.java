package es.pgomez.tc.testing.domain.model.common;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
class ColorTypeTest {


    @Test
    void nullColorTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new ColorType(null));
    }

    @Test
    void emptyColorTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new ColorType(""));
    }

    @Test
    void blankColorTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new ColorType("  "));
    }

    @Test
    void colorTest() {
        ColorType colorType = new ColorType("red");
        Assertions.assertEquals("red", colorType.color());
    }
}
