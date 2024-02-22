package es.pgomez.tc.testing.infrastructure.adapter.mongo.repository;

import es.pgomez.tc.testing.domain.model.Tester;
import es.pgomez.tc.testing.domain.model.common.util.Sex;
import es.pgomez.tc.testing.domain.ports.output.TesterRepository;
import es.pgomez.tc.testing.infrastructure.adapter.mongo.panache.MongoTesterRepository;
import io.quarkus.mongodb.panache.PanacheQuery;
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
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@QuarkusTest
class TesterRepositoryMongoTest {

    @InjectMock
    MongoTesterRepository mongoTesterRepository;

    @Inject
    TesterRepository testerRepository;

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

    @Test
    void getByIdTest() throws InstanceNotFoundException {
        UUID id = UUID.randomUUID();
        Tester tester = getTester();
        Optional<Tester> testerOptional = Optional.of(tester);
        Mockito.when(mongoTesterRepository.findByIdOptional(id.toString())).thenReturn(testerOptional);

        Tester foundTester = testerRepository.getById(id);
        Assertions.assertEquals(tester, foundTester);
    }

    @Test
    void getByNonExitentIdTest() throws InstanceNotFoundException {
        UUID id = UUID.randomUUID();
        Optional<Tester> testerOptional = Optional.empty();
        Mockito.when(mongoTesterRepository.findByIdOptional(id.toString())).thenReturn(testerOptional);

        Assertions.assertThrows(InstanceNotFoundException.class, () ->
                testerRepository.getById(id));
    }

    @Test
    void getAllTest() {
        PanacheQuery<Tester> query = Mockito.mock(PanacheQuery.class);
        Mockito.when(mongoTesterRepository.findAll()).thenReturn(query);
        Mockito.when(query.stream()).thenReturn(Stream.of(getTester()));

        List<Tester> Testers = testerRepository.getAll(null, null);
        Assertions.assertEquals(1, Testers.size());
    }

    @Test
    void countTest() {
        Mockito.when(mongoTesterRepository.count()).thenReturn(4L);

        Long count = testerRepository.count();
        Assertions.assertEquals(4l, count);
    }

    @Test
    void createTesterTest() {
        Tester tester = getTester();
        Mockito.doNothing().when(mongoTesterRepository).persist(tester);

        testerRepository.createTester(tester);
        Assertions.assertTrue(true);
    }

    @Test
    void updateTesterTest() throws InstanceNotFoundException {
        UUID id = UUID.randomUUID();
        Tester tester = getTester();
        Optional<Tester> testerOptional = Optional.of(tester);
        Mockito.when(mongoTesterRepository.findByIdOptional(id.toString())).thenReturn(testerOptional);
        Mockito.doNothing().when(mongoTesterRepository).update(tester);

        testerRepository.updateTester(id, tester);
        Assertions.assertTrue(true);
    }

    @Test
    void updateNonExistentTester() throws InstanceNotFoundException {
        UUID id = UUID.randomUUID();
        Tester tester = getTester();
        Optional<Tester> testerOptional = Optional.empty();
        Mockito.when(mongoTesterRepository.findByIdOptional(id.toString())).thenReturn(testerOptional);

        Assertions.assertThrows(InstanceNotFoundException.class, () ->
                testerRepository.updateTester(id, tester));
    }

    @Test
    void deleteTesterTest() throws InstanceNotFoundException {
        UUID id = UUID.randomUUID();
        Mockito.when(mongoTesterRepository.deleteById(id.toString())).thenReturn(true);

        testerRepository.deleteTester(id);
        Assertions.assertTrue(true);
    }

    @Test
    void deleteNonExistentTesterTest() {
        UUID id = UUID.randomUUID();
        Mockito.when(mongoTesterRepository.deleteById(id.toString())).thenReturn(false);

        Assertions.assertThrows(InstanceNotFoundException.class, () ->
                testerRepository.deleteTester(id));

    }
}
