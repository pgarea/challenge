package es.pgomez.tc.testing.domain.model.common;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

@QuarkusTest
class CreationDateTypeTest {

    @Test
    void nullCreationDateTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new CreationDateType(null));
    }

    @Test
    void creationDateAfterNowTest() {
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(1);
        Assertions.assertThrows(ValidationException.class, () ->
                new CreationDateType(localDateTime));
    }

    @Test
    void creationDateSameDateAsNowTest() {
        LocalDateTime localDate = LocalDateTime.now();
        CreationDateType creationDateType = new CreationDateType(localDate);
        Assertions.assertEquals(localDate, creationDateType.creationDate());
    }
    
    @Test
    void creationDateTest() {
        LocalDateTime localDateTime = LocalDateTime.of(2020, 1, 2, 3, 4);
        CreationDateType creationDateType = new CreationDateType(localDateTime);
        Assertions.assertEquals(localDateTime, creationDateType.creationDate());
    }
}
