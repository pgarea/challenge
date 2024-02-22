package es.pgomez.tc.testing.infrastructure.adapter.mongo.repository;

import es.pgomez.tc.testing.domain.model.Test;
import es.pgomez.tc.testing.domain.ports.output.TestRepository;
import es.pgomez.tc.testing.infrastructure.adapter.mongo.panache.MongoTestRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class TestRepositoryMongoImpl implements TestRepository {

    private final MongoTestRepository mongoTestRepository;

    public TestRepositoryMongoImpl(MongoTestRepository mongoTestRepository) {
        this.mongoTestRepository = mongoTestRepository;
    }

    @Override
    public Test registerTest(Test test) {
        mongoTestRepository.persist(test);
        return test;
    }

    @Override
    public void deleteByProductId(UUID productId) {
        mongoTestRepository.delete("productId", productId.toString());
    }

    @Override
    public void deleteByTesterId(UUID testerId) {
        mongoTestRepository.delete("testerId", testerId.toString());

    }
}
