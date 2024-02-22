package es.pgomez.tc.testing.domain.model.common;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@QuarkusTest
class LogoTypeTest {

    @Test
    void nullLogoUrlTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new LogoType(null));
    }

    @Test
    void emptyLogoUrlTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new LogoType(""));
    }

    @Test
    void blankLogoUrlTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new LogoType("  "));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "http://www.test.es",
            "http://test.com",
            "https://www.test.es",
            "https://test.com",
            "https://test.com/test",
            "https://test.com/test.png",
    })
    void validLogoUrlTest(String url) {
        LogoType logoType = new LogoType(url);
        Assertions.assertEquals(url, logoType.logo());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "test",
            "test.com"
    })
    void invalidLogoUrlTest(String url) {
        Assertions.assertThrows(ValidationException.class, () ->
                new LogoType(url));
    }

}
