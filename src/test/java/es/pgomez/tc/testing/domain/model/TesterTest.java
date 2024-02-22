package es.pgomez.tc.testing.domain.model;

import es.pgomez.tc.testing.domain.model.common.util.Sex;
import es.pgomez.tc.testing.util.auth.Crypto;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@QuarkusTest
class TesterTest {

    private Tester getTester() {
        UUID id = UUID.randomUUID();
        String name = "testerName";
        String email = "tester@email.com";
        String password = "password";
        LocalDate birthdate = LocalDate.now().minusDays(2);
        Sex sex = Sex.FEMALE;
        Long testDone = 0L;
        LocalDateTime creationDate = LocalDateTime.now().minusDays(1);
        Double height = 123d;
        Double weight = 234d;
        return new Tester(id, name, email, password, birthdate, sex, testDone, creationDate, height, weight);
    }

    @Test
    void setRawPasswordTest() {
        Tester tester = getTester();
        tester.setRawPassword("12345678");
        Assertions.assertEquals("12345678", tester.getPassword().password());
    }

    @Test
    void matchesPasswordTest() {
        Tester tester = getTester();
        Assertions.assertEquals(Crypto.hashPassword("password"), tester.getPassword().password());
    }

    @Test
    void updateTestDoneTest() {
        Tester tester = getTester();
        tester.updateTestDone();
        Assertions.assertEquals(1, tester.getTestDone().testDone());
    }

}
