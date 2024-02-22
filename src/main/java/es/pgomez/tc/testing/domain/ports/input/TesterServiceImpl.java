package es.pgomez.tc.testing.domain.ports.input;

import es.pgomez.tc.testing.domain.model.Tester;
import es.pgomez.tc.testing.domain.ports.dto.PageableDTO;
import es.pgomez.tc.testing.domain.ports.dto.TesterCreationDTO;
import es.pgomez.tc.testing.domain.ports.dto.TesterDTO;
import es.pgomez.tc.testing.domain.ports.input.interfaces.TesterService;
import es.pgomez.tc.testing.domain.ports.output.TesterRepository;
import es.pgomez.tc.testing.util.auth.Role;
import es.pgomez.tc.testing.util.auth.TokenDTO;
import es.pgomez.tc.testing.util.auth.TokenUtils;
import io.quarkus.vertx.ConsumeEvent;
import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ValidationException;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.management.InstanceNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class TesterServiceImpl implements TesterService {

    private final EventBus bus;

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    private final TesterRepository testerRepository;

    public TesterServiceImpl(EventBus bus, TesterRepository testerRepository) {
        this.bus = bus;
        this.testerRepository = testerRepository;
    }

    @Override
    public TesterDTO getById(UUID id) throws InstanceNotFoundException {
        Tester tester = testerRepository.getById(id);
        return new TesterDTO(tester);
    }

    @Override
    public TokenDTO login(String email, String password) {
        try {
            Tester tester = testerRepository.getByEmail(email);
            if (tester.matchesPassword(password)) {
                return new TokenDTO(TokenUtils.generateToken(tester.getId(), Role.TESTER, issuer));
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public PageableDTO<TesterDTO> getAll(Long page, Long size) {
        List<Tester> testers = testerRepository.getAll(page, size);
        Long count = testerRepository.count();
        return new PageableDTO<>(testers.stream().map(TesterDTO::new).toList(), count);
    }

    @Override
    public TesterDTO createTester(TesterCreationDTO tester) throws ValidationException {
        Tester newTester = new Tester(null, tester.getName(), tester.getEmail(), tester.getPassword(),
                tester.getBirthdate(), tester.getSex(), 0L, tester.getMeasuresCreationDate(),
                tester.getHeightCm(), tester.getWeightKg());

        return new TesterDTO(testerRepository.createTester(newTester));
    }

    @Override
    public TesterDTO updateTester(UUID id, TesterCreationDTO tester) throws ValidationException, InstanceNotFoundException {
        Tester oldTester = testerRepository.getById(id);
        if (oldTester.getMeasures().getHeightCm().height() != tester.getHeightCm().doubleValue() ||
                oldTester.getMeasures().getWeightKg().weight() != tester.getWeightKg().doubleValue()) {
            tester.setMeasuresCreationDate(LocalDateTime.now());
        }
        Tester newTester = new Tester(id, tester.getName(), oldTester.getEmail().email(), tester.getPassword(),
                tester.getBirthdate(), tester.getSex(), oldTester.getTestDone().testDone(),
                tester.getMeasuresCreationDate(), tester.getHeightCm(), tester.getWeightKg());

        return new TesterDTO(testerRepository.updateTester(id, newTester));
    }

    @Override
    public void deleteTester(UUID id) throws InstanceNotFoundException {
        testerRepository.deleteTester(id);
        bus.send("DELETED_TESTER", id);
    }

    @ConsumeEvent("NEW_TEST")
    void updateTestDone(UUID testerId) throws InstanceNotFoundException {
        Tester tester = testerRepository.getById(testerId);
        tester.updateTestDone();
        testerRepository.updateTester(testerId, tester);
    }
}
