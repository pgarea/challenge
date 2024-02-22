package es.pgomez.tc.testing.domain.ports.input.interfaces;

import es.pgomez.tc.testing.domain.ports.dto.TestDTO;

import javax.management.InstanceNotFoundException;
import java.util.UUID;

public interface ReportingService {

    void reportNewTest(UUID userId, TestDTO testDTO) throws InstanceNotFoundException;
}
