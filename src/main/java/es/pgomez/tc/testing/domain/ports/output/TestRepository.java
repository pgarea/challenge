package es.pgomez.tc.testing.domain.ports.output;

import es.pgomez.tc.testing.domain.model.Test;

import java.util.UUID;

public interface TestRepository {

    Test registerTest(Test test);

    void deleteByProductId(UUID productId);

    void deleteByTesterId(UUID testerId);
}
