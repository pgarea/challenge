package es.pgomez.tc.testing.domain.ports.input;

import es.pgomez.tc.testing.domain.model.Product;
import es.pgomez.tc.testing.domain.model.Tester;
import es.pgomez.tc.testing.domain.model.common.util.Sex;
import es.pgomez.tc.testing.domain.ports.dto.TestDTO;
import es.pgomez.tc.testing.domain.ports.input.interfaces.ReportingService;
import es.pgomez.tc.testing.domain.ports.output.ProductRepository;
import es.pgomez.tc.testing.domain.ports.output.TestRepository;
import es.pgomez.tc.testing.domain.ports.output.TesterRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@QuarkusTest
class ReportingServiceTest {

    @InjectMock
    TesterRepository testerRepository;

    @InjectMock
    TestRepository testRepository;

    @InjectMock
    ProductRepository productRepository;

    @Inject
    ReportingService reportingService;

    private Tester getTester() {
        UUID id = UUID.randomUUID();
        String name = "Tester";
        String email = "test@test.com";
        LocalDate birthdate = LocalDate.now().minusDays(2);
        String password = "password";
        Double height = 100d;
        Double weight = 80d;
        LocalDateTime creationDate = LocalDateTime.of(2020, 1, 1, 10, 10);
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
    void reportNewTestTest() throws InstanceNotFoundException {
        TestDTO testDTO = new TestDTO(getTest());
        Mockito.when(testerRepository.getById(Mockito.any(UUID.class))).thenReturn(getTester());
        Mockito.when(productRepository.getById(Mockito.any(UUID.class))).thenReturn(getProduct());
        Mockito.when(testRepository.registerTest(Mockito.any(es.pgomez.tc.testing.domain.model.Test.class))).thenReturn(getTest());

        reportingService.reportNewTest(UUID.randomUUID(), testDTO);
        Assertions.assertTrue(true);
    }


    @Test
    void reportNewTestForNonExistingProductTest() throws InstanceNotFoundException {
        TestDTO testDTO = new TestDTO(getTest());
        Mockito.when(testerRepository.getById(Mockito.any(UUID.class))).thenReturn(getTester());
        Mockito.when(productRepository.getById(Mockito.any(UUID.class))).thenThrow(new InstanceNotFoundException());

        Assertions.assertThrows(InstanceNotFoundException.class, () ->
                reportingService.reportNewTest(UUID.randomUUID(), testDTO));

    }

    @Test
    void reportNewTestForNonExistingUserTest() throws InstanceNotFoundException {
        TestDTO testDTO = null;
        Mockito.when(testerRepository.getById(Mockito.any(UUID.class))).thenThrow(new InstanceNotFoundException());

        Assertions.assertThrows(InstanceNotFoundException.class, () ->
                reportingService.reportNewTest(UUID.randomUUID(), testDTO));

    }

}
