package es.pgomez.tc.testing.domain.model.common;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
class TestDoneTypeTest {

    @Test
    void nullTestDoneTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new TestDoneType(null));
    }

    @Test
    void negativeTestDoneTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new TestDoneType(-1L));
    }

    @Test
    void zeroTestDoneTest() {
        TestDoneType testDoneType = new TestDoneType(0L);
        Assertions.assertEquals(0L, testDoneType.testDone());
    }
    
    @Test
    void positiveTestDoneTest() {
        TestDoneType testDoneType = new TestDoneType(1L);
        Assertions.assertEquals(1L, testDoneType.testDone());
    }
}
