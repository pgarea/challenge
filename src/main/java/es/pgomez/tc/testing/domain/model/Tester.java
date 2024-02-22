package es.pgomez.tc.testing.domain.model;

import es.pgomez.tc.testing.domain.model.common.*;
import es.pgomez.tc.testing.domain.model.common.util.Sex;
import es.pgomez.tc.testing.util.auth.Crypto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Tester {

    private UUID id;

    private NameType name;

    private EmailType email;

    private PasswordType password;

    private BirthdateType birthdate;

    private SexType sex;

    private TestDoneType testDone;

    private Measure measures;

    public Tester(UUID id, String name, String email, LocalDate birthdate, Sex sex,
                  Long testDone, LocalDateTime creationDate, Double height, Double weight) {
        this.id = id;
        this.name = new NameType(name);
        this.email = new EmailType(email);
        this.birthdate = new BirthdateType(birthdate);
        this.sex = new SexType(sex);
        this.testDone = new TestDoneType(testDone);
        this.measures = new Measure(creationDate, height, weight);
    }

    public Tester(UUID id, String name, String email, String password, LocalDate birthdate, Sex sex,
                  Long testDone, LocalDateTime creationDate, Double height, Double weight) {
        this.id = id;
        this.name = new NameType(name);
        this.email = new EmailType(email);
        this.password = new PasswordType(password, false);
        this.birthdate = new BirthdateType(birthdate);
        this.sex = new SexType(sex);
        this.testDone = new TestDoneType(testDone);
        this.measures = new Measure(creationDate, height, weight);
    }

    public void setRawPassword(String password) {
        this.password = new PasswordType(password, true);
    }

    public boolean matchesPassword(String password) {
        return Crypto.hashPassword(password).equals(this.password.password());
    }

    public void updateTestDone() {
        this.testDone = new TestDoneType(this.testDone.testDone() + 1);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public NameType getName() {
        return name;
    }

    public EmailType getEmail() {
        return email;
    }

    public PasswordType getPassword() {
        return password;
    }

    public BirthdateType getBirthdate() {
        return birthdate;
    }

    public SexType getSex() {
        return sex;
    }

    public TestDoneType getTestDone() {
        return testDone;
    }

    public Measure getMeasures() {
        return measures;
    }

    @Override
    public String toString() {
        return "Tester{" +
                "id=" + id +
                ", name=" + name +
                ", email=" + email +
                ", password=" + password +
                ", birthdate=" + birthdate +
                ", sex=" + sex +
                ", testDone=" + testDone +
                ", measures=" + measures +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tester tester = (Tester) o;
        return Objects.equals(id, tester.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, password, birthdate, sex, testDone, measures);
    }
}
