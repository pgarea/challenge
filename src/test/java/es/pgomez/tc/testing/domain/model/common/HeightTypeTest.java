package es.pgomez.tc.testing.domain.model.common;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
class HeightTypeTest {

    @Test
    void nullHeightTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new HeightType(null));
    }

    @Test
    void negativeHeightTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new HeightType(-0.1));
    }

    @Test
    void zeroHeightTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new HeightType(0d));
    }

    @Test
    void positiveHeightTest() {
        HeightType heightType = new HeightType(.1);
        Assertions.assertEquals(.1d, heightType.height());
    }

    @Test
    void positiveWithMultipleDecimalsHeightTest() {
        HeightType heightType = new HeightType(.1324);
        Assertions.assertEquals(.1d, heightType.height());
    }

    @Test
    void positiveWithMultipleDecimals2HeightTest() {
        HeightType heightType = new HeightType(.1524);
        Assertions.assertEquals(.2d, heightType.height());
    }
}
