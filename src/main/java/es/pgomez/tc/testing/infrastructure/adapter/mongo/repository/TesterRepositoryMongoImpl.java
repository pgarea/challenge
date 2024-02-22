package es.pgomez.tc.testing.infrastructure.adapter.mongo.repository;

import es.pgomez.tc.testing.domain.model.Tester;
import es.pgomez.tc.testing.domain.ports.output.TesterRepository;
import es.pgomez.tc.testing.infrastructure.adapter.mongo.panache.MongoTesterRepository;
import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class TesterRepositoryMongoImpl implements TesterRepository {

    private final MongoTesterRepository mongoTesterRepository;

    public TesterRepositoryMongoImpl(MongoTesterRepository mongoTesterRepository) {
        this.mongoTesterRepository = mongoTesterRepository;
    }

    @Override
    public Tester getById(UUID id) throws InstanceNotFoundException {
        Optional<Tester> testerOptional = mongoTesterRepository.findByIdOptional(id.toString());
        if (testerOptional.isPresent()) {
            return testerOptional.get();
        }
        throw new InstanceNotFoundException();
    }

    @Override
    public Tester getByEmail(String email) throws InstanceNotFoundException {
        Optional<Tester> testerOptional = mongoTesterRepository.find("email", email).firstResultOptional();
        if (testerOptional.isPresent()) {
            return testerOptional.get();
        }
        throw new InstanceNotFoundException();
    }


    @Override
    public List<Tester> getAll(Long page, Long size) {
        PanacheQuery<Tester> query = mongoTesterRepository.findAll();
        if (page != null && size != null) {
            query = query.page(new Page(page.intValue(), size.intValue()));
        }
        return query.stream().toList();
    }

    @Override
    public Long count() {
        return mongoTesterRepository.count();
    }

    @Override
    public Tester createTester(Tester tester) {
        mongoTesterRepository.persist(tester);
        return tester;
    }

    @Override
    public Tester updateTester(UUID id, Tester tester) throws InstanceNotFoundException {
        Optional<Tester> testerOptional = mongoTesterRepository.findByIdOptional(id.toString());
        if (testerOptional.isPresent()) {
            mongoTesterRepository.update(tester);
            return tester;
        }
        throw new InstanceNotFoundException();
    }

    @Override
    public void deleteTester(UUID id) throws InstanceNotFoundException {
        boolean deletedElements = mongoTesterRepository.deleteById(id.toString());
        if (!deletedElements) {
            throw new InstanceNotFoundException();
        }
    }
}
