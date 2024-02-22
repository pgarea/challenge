package es.pgomez.tc.testing.domain.ports.input.interfaces;

import es.pgomez.tc.testing.domain.ports.dto.PageableDTO;
import es.pgomez.tc.testing.domain.ports.dto.TesterCreationDTO;
import es.pgomez.tc.testing.domain.ports.dto.TesterDTO;
import es.pgomez.tc.testing.util.auth.TokenDTO;
import jakarta.validation.ValidationException;

import javax.management.InstanceNotFoundException;
import java.util.UUID;

public interface TesterService {

    TesterDTO getById(UUID id) throws InstanceNotFoundException;

    TokenDTO login(String email, String password);

    PageableDTO<TesterDTO> getAll(Long page, Long size);

    TesterDTO createTester(TesterCreationDTO tester) throws ValidationException;

    TesterDTO updateTester(UUID id, TesterCreationDTO tester) throws ValidationException, InstanceNotFoundException;

    void deleteTester(UUID id) throws InstanceNotFoundException;

}
