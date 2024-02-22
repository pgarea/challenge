package es.pgomez.tc.testing.domain.model.common;

import es.pgomez.tc.testing.domain.model.common.util.Sex;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
class SexTypeTest {

    @Test
    void nullEmailTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new SexType(null));
    }

    @Test
    void femaleSexTest() {
        SexType sexType = new SexType(Sex.FEMALE);
        Assertions.assertEquals(Sex.FEMALE, sexType.sex());
    }


    @Test
    void maleSexTest() {
        SexType sexType = new SexType(Sex.MALE);
        Assertions.assertEquals(Sex.MALE, sexType.sex());
    }
}
