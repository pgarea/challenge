package es.pgomez.tc.testing.domain.model.common;

import io.quarkus.runtime.util.HashUtil;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
class PasswordTypeTest {

    /**
     * Test null password
     */
    @Test
    void nullPasswordTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new PasswordType(null, false));
    }

    /**
     * Test length 0 password
     */
    @Test
    void emptyPasswordTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new PasswordType("", false));
    }

    /**
     * Test invalid 8 length password with white spaces
     */
    @Test
    void blankPasswordTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new PasswordType(" 123456 ", false));
    }

    /**
     * Test 7 length valid password
     */
    @Test
    void validSevenLengthPasswordTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new PasswordType("1234567", false));
    }

    /**
     * Test 8 length valid password
     */
    @Test
    void validEightLengthPasswordTest() {
        PasswordType passwordType = new PasswordType("12345678", false);
        Assertions.assertEquals(HashUtil.sha1("12345678"), passwordType.password());
    }

    
    @Test
    void validTwentyLengthPasswordTest() {
        PasswordType passwordType = new PasswordType("12345678910234567890", false);
        Assertions.assertEquals(HashUtil.sha1("12345678910234567890"), passwordType.password());
    }

    /**
     * Test 20 length valid password
     */
    @Test
    void validTwentyOneLengthPasswordTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new PasswordType("123456789102345678901", false));
    }
}
