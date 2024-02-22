package es.pgomez.tc.testing.domain.model.common;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@QuarkusTest
class SizesTypeTest {

    @Test
    void nullSizeListTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new SizesType(null));
    }

    @Test
    void emptySizeListTest() {
        SizesType sizesType = new SizesType(new ArrayList<>());
        Assertions.assertTrue(sizesType.sizes().isEmpty());
    }
    
    @Test
    void populatedSizeListTest() {
        SizesType sizesType = new SizesType(List.of("M"));
        Assertions.assertEquals(1, sizesType.sizes().size());
        Assertions.assertEquals("M", sizesType.sizes().get(0).size());
    }

}
