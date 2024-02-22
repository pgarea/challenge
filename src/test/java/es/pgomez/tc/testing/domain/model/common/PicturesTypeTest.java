package es.pgomez.tc.testing.domain.model.common;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

@QuarkusTest
class PicturesTypeTest {

    @Test
    void nullPicturesListTest() {
        Assertions.assertThrows(ValidationException.class, () ->
                new PicturesType(null));
    }

    @Test
    void emptySizeListTest() {
        PicturesType picturesType = new PicturesType(new ArrayList<>());
        Assertions.assertTrue(picturesType.pictures().isEmpty());
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
    void populatedSizeListTest(String url) {
        List<String> pictures = new ArrayList<>();
        pictures.add(url);
        PicturesType picturesType = new PicturesType(pictures);
        Assertions.assertEquals(1, picturesType.pictures().size());
        Assertions.assertEquals(url, picturesType.pictures().get(0));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "test",
            "test.com"
    })
    void invalidSizeListTest(String url) {
        List<String> pictures = new ArrayList<>();
        pictures.add(url);
        Assertions.assertThrows(ValidationException.class, () ->
                new PicturesType(pictures));
    }
}
