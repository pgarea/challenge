package es.pgomez.tc.testing.domain.model.common;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;


@QuarkusTest
class BirthdateTypeTest {

    @Test
    @DisplayName("Create a birthdate with null date")
    void nullBirthdateTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new BirthdateType(null));
    }

    @Test
    @DisplayName("Create a birthdate with a future date")
    void birthdateAfterNowTest() {
        LocalDate localDate = LocalDate.now().plusDays(1);
        Assertions.assertThrows(ValidationException.class, () ->
                new BirthdateType(localDate));
    }

    @Test
    @DisplayName("Create a birthdate with the today date")
    void birthdateSameDateAsNowTest() {
        LocalDate localDate = LocalDate.now();
        Assertions.assertThrows(ValidationException.class, () ->
                new BirthdateType(localDate));
    }

    @Test
    @DisplayName("Create a birthdate with a past date")
    void birthDateTest() {
        LocalDate localDate = LocalDate.of(2020, 1, 2);
        BirthdateType birthdateType = new BirthdateType(localDate);
        Assertions.assertEquals(localDate, birthdateType.birthdate());
    }
}
