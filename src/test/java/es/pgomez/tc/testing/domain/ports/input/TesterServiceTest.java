package es.pgomez.tc.testing.domain.ports.input;

import es.pgomez.tc.testing.domain.model.Tester;
import es.pgomez.tc.testing.domain.model.common.util.Sex;
import es.pgomez.tc.testing.domain.ports.dto.PageableDTO;
import es.pgomez.tc.testing.domain.ports.dto.TesterCreationDTO;
import es.pgomez.tc.testing.domain.ports.dto.TesterDTO;
import es.pgomez.tc.testing.domain.ports.input.interfaces.TesterService;
import es.pgomez.tc.testing.domain.ports.output.TesterRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@QuarkusTest
class TesterServiceTest {

    @InjectMock
    TesterRepository testerRepository;

    @Inject
    TesterService testerService;

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

    private TesterCreationDTO getTesterCreation() {
        TesterCreationDTO testerCreationDTO = new TesterCreationDTO();
        testerCreationDTO.setName("Tester");
        testerCreationDTO.setEmail("test@test.com");
        testerCreationDTO.setSex(Sex.FEMALE);
        testerCreationDTO.setBirthdate(LocalDate.now().minusDays(2));
        testerCreationDTO.setPassword("password");
        testerCreationDTO.setHeightCm(100d);
        testerCreationDTO.setWeightKg(80d);
        testerCreationDTO.setMeasuresCreationDate(LocalDateTime.of(2020, 1, 1, 10, 10));
        return testerCreationDTO;
    }

    @Test
    void getByIdTest() throws InstanceNotFoundException {
        Tester tester = getTester();
        Mockito.when(testerRepository.getById(tester.getId())).thenReturn(tester);
        TesterDTO found = testerService.getById(tester.getId());
        Assertions.assertEquals(tester.getId(), found.getId());
    }

    @Test
    void getByNonExistingIdTest() throws InstanceNotFoundException {
        Tester tester = getTester();
        Mockito.when(testerRepository.getById(tester.getId())).thenThrow(new InstanceNotFoundException());
        Assertions.assertThrows(InstanceNotFoundException.class, () ->
                testerService.getById(tester.getId()));
    }

    @Test
    void getAllTest() {
        Tester tester = getTester();
        List<TesterDTO> testerDTOList = List.of(new TesterDTO(tester));
        PageableDTO<TesterDTO> pageableDTO = new PageableDTO<>(testerDTOList, 1L);
        Mockito.when(testerRepository.getAll(null, null)).thenReturn(List.of(tester));
        Mockito.when(testerRepository.count()).thenReturn(1L);

        PageableDTO<TesterDTO> testers = testerService.getAll(null, null);
        Assertions.assertEquals(pageableDTO.getCount(), testers.getCount());
        Assertions.assertEquals(pageableDTO.getValues(), testers.getValues());
    }

    @Test
    void createTesterTest() throws ValidationException {
        Tester tester = getTester();
        TesterCreationDTO testerCreationDTO = getTesterCreation();
        Mockito.when(testerRepository.createTester(Mockito.any(Tester.class))).thenReturn(tester);

        TesterDTO createdTester = testerService.createTester(testerCreationDTO);
        Assertions.assertEquals(createdTester.getId(), tester.getId());
    }

    @Test
    void createInvalidTesterTest() throws ValidationException {
        TesterCreationDTO testerCreationDTO = getTesterCreation();
        Mockito.when(testerRepository.createTester(Mockito.any(Tester.class))).thenThrow(new ValidationException());

        Assertions.assertThrows(ValidationException.class, () ->
                testerService.createTester(testerCreationDTO));
    }

    @Test
    void updateTesterTest() throws ValidationException, InstanceNotFoundException {
        TesterCreationDTO testerCreationDTO = getTesterCreation();
        Tester tester = getTester();
        Mockito.when(testerRepository.getById(tester.getId())).thenReturn(tester);
        Mockito.when(testerRepository.updateTester(Mockito.any(UUID.class), Mockito.any(Tester.class)))
                .thenReturn(tester);

        TesterDTO updatedTester = testerService.updateTester(tester.getId(), testerCreationDTO);
        Assertions.assertEquals(tester.getId(), updatedTester.getId());
        Assertions.assertEquals(tester.getEmail().email(), updatedTester.getEmail());
    }

    @Test
    void updateNonExistentTesterTest() throws ValidationException, InstanceNotFoundException {
        TesterCreationDTO testerCreationDTO = getTesterCreation();
        Tester tester = getTester();
        Mockito.when(testerRepository.getById(tester.getId())).thenReturn(tester);
        Mockito.when(testerRepository.updateTester(Mockito.any(UUID.class), Mockito.any(Tester.class)))
                .thenThrow(new InstanceNotFoundException());

        Assertions.assertThrows(InstanceNotFoundException.class, () ->
                testerService.updateTester(tester.getId(), testerCreationDTO));

    }

    @Test
    void deleteTesterTest() throws InstanceNotFoundException {
        UUID uuid = UUID.randomUUID();
        Mockito.doNothing().when(testerRepository).deleteTester(uuid);
        testerService.deleteTester(uuid);
        Assertions.assertTrue(true);
    }

    @Test
    void deleteNonExistingTesterTest() throws InstanceNotFoundException {
        UUID uuid = UUID.randomUUID();
        Mockito.doThrow(new InstanceNotFoundException()).when(testerRepository).deleteTester(uuid);
        Assertions.assertThrows(InstanceNotFoundException.class, () ->
                testerService.deleteTester(uuid));
    }
}
