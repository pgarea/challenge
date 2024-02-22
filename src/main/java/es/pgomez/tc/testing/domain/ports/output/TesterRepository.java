package es.pgomez.tc.testing.domain.ports.output;

import es.pgomez.tc.testing.domain.model.Tester;
import jakarta.validation.ValidationException;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.UUID;

public interface TesterRepository {

    Tester getById(UUID id) throws InstanceNotFoundException;

    Tester getByEmail(String email) throws InstanceNotFoundException;

    List<Tester> getAll(Long page, Long size);

    Long count();

    Tester createTester(Tester tester) throws ValidationException;

    Tester updateTester(UUID id, Tester tester) throws InstanceNotFoundException;

    void deleteTester(UUID id) throws InstanceNotFoundException;
}
