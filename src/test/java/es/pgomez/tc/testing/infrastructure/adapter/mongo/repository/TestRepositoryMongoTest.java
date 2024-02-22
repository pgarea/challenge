package es.pgomez.tc.testing.infrastructure.adapter.mongo.repository;

import es.pgomez.tc.testing.domain.model.Product;
import es.pgomez.tc.testing.domain.model.Tester;
import es.pgomez.tc.testing.domain.model.common.util.Sex;
import es.pgomez.tc.testing.domain.ports.output.TestRepository;
import es.pgomez.tc.testing.infrastructure.adapter.mongo.panache.MongoTestRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@QuarkusTest
class TestRepositoryMongoTest {

    @InjectMock
    MongoTestRepository mongoTestRepository;

    @Inject
    TestRepository testRepository;

    private Tester getTester() {
        UUID id = UUID.randomUUID();
        String name = "Tester";
        String email = "test@test.com";
        LocalDate birthdate = LocalDate.now().minusDays(2);
        String password = "password";
        Double height = 100d;
        Double weight = 80d;
        LocalDateTime creationDate = LocalDateTime.now().minusDays(1);
        return new Tester(id, name, email, password, birthdate, Sex.FEMALE, 0L, creationDate, height, weight);
    }

    private Product getProduct() {
        UUID id = UUID.randomUUID();
        String sku = "skuTest";
        List<String> sizes = List.of("L");
        List<String> pictures = List.of("http://pictures.com");
        String brandName = "brandNameTest";
        String brandLogo = "http://logo.com";
        String color = "red";
        return new Product(id, sku, sizes, pictures, brandName, brandLogo, color);
    }

    private es.pgomez.tc.testing.domain.model.Test getTest() {
        return new es.pgomez.tc.testing.domain.model.Test(getTester(), getProduct(), "L");
    }

    @Test
    void createTestTest() {
        es.pgomez.tc.testing.domain.model.Test test = getTest();
        Mockito.doNothing().when(mongoTestRepository).persist(test);

        testRepository.registerTest(test);
        Assertions.assertTrue(true);
    }
}
