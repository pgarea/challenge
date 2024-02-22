package es.pgomez.tc.testing.domain.model.common;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
class NameTypeTest {

    @Test
    void nullNameTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new NameType(null));
    }

    @Test
    void emptyNameTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new NameType(""));
    }

    @Test
    void blankNameTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new NameType("  "));
    }
    
    @Test
    void nameTest() {
        NameType nameType = new NameType("Test");
        Assertions.assertEquals("Test", nameType.name());
    }
}
