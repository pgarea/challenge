package es.pgomez.tc.testing.domain.model.common;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
class EmailTypeTest {

    @Test
    void nullEmailTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new EmailType(null));
    }

    @Test
    void emptyEmailTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new EmailType(""));
    }

    @Test
    void blankEmailTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new EmailType("  "));
    }

    @Test
    void emailWithoutDomainTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new EmailType("test@test"));
    }

    @Test
    void emailWithoutAtTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new EmailType("testtest.com"));
    }

    @Test
    void correctSubdomainDomainEmailTest() {
        EmailType emailType = new EmailType("test@test.es.com");
        Assertions.assertEquals("test@test.es.com", emailType.email());
    }

    @Test
    void correctEmailTest() {
        EmailType emailType = new EmailType("test@test.com");
        Assertions.assertEquals("test@test.com", emailType.email());
    }
}
