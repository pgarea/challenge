package es.pgomez.tc.testing.domain.model.common;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
class WeightTypeTest {

    @Test
    void nullWeightTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new WeightType(null));
    }

    @Test
    void negativeWeightTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new WeightType(-0.1));
    }

    @Test
    void zeroWeightTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new WeightType(0d));
    }

    @Test
    void positiveWeightTest() {
        WeightType weightType = new WeightType(.1);
        Assertions.assertEquals(.1d, weightType.weight());
    }

    @Test
    void positiveWithMultipleDecimalsRoundingDownWeightTest() {
        WeightType weightType = new WeightType(.1324);
        Assertions.assertEquals(.1d, weightType.weight());
    }

    @Test
    void positiveWithMultipleDecimalsRoundingUpWeightTest() {
        WeightType weightType = new WeightType(.1524);
        Assertions.assertEquals(.2d, weightType.weight());
    }
}
