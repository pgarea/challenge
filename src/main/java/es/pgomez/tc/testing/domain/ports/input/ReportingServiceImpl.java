package es.pgomez.tc.testing.domain.ports.input;

import es.pgomez.tc.testing.domain.model.Product;
import es.pgomez.tc.testing.domain.model.Test;
import es.pgomez.tc.testing.domain.model.Tester;
import es.pgomez.tc.testing.domain.ports.dto.TestDTO;
import es.pgomez.tc.testing.domain.ports.input.interfaces.ReportingService;
import es.pgomez.tc.testing.domain.ports.output.ProductRepository;
import es.pgomez.tc.testing.domain.ports.output.TestRepository;
import es.pgomez.tc.testing.domain.ports.output.TesterRepository;
import io.quarkus.vertx.ConsumeEvent;
import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;

import javax.management.InstanceNotFoundException;
import java.util.UUID;

@ApplicationScoped
public class ReportingServiceImpl implements ReportingService {

    private final EventBus bus;
    private final TesterRepository testerRepository;
    private final TestRepository testRepository;
    private final ProductRepository productRepository;

    public ReportingServiceImpl(EventBus bus,
                                TesterRepository testerRepository,
                                TestRepository testRepository,
                                ProductRepository productRepository) {
        this.bus = bus;
        this.testerRepository = testerRepository;
        this.testRepository = testRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void reportNewTest(UUID testerId, TestDTO testDTO) throws InstanceNotFoundException {
        Tester tester = testerRepository.getById(testerId);
        Product product = productRepository.getById(testDTO.getProductId());

        Test test = new Test(tester, product, testDTO.getSize());
        testRepository.registerTest(test);
        bus.publish("NEW_TEST", testerId);
    }

    @ConsumeEvent("DELETED_PRODUCT")
    void deleteByProductId(UUID id) {
        testRepository.deleteByProductId(id);
    }

    @ConsumeEvent("DELETED_TESTER")
    void deleteByTesterId(UUID id) {
        testRepository.deleteByTesterId(id);
    }
}
