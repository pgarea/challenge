package es.pgomez.tc.testing.domain.ports.dto;

import es.pgomez.tc.testing.domain.model.Tester;
import es.pgomez.tc.testing.domain.model.common.util.Sex;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class TesterDTO {

    private UUID id;

    private String name;

    private String email;

    private LocalDate birthdate;

    private Sex sex;

    private Long testDone;

    private MeasureDTO measures;

    public TesterDTO() {
    }

    public TesterDTO(Tester tester) {
        this.id = tester.getId();
        this.name = tester.getName().name();
        this.email = tester.getEmail().email();
        this.birthdate = tester.getBirthdate().birthdate();
        this.sex = tester.getSex().sex();
        this.testDone = tester.getTestDone().testDone();
        this.measures = new MeasureDTO(tester.getMeasures());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public Long getTestDone() {
        return testDone;
    }

    public void setTestDone(Long testDone) {
        this.testDone = testDone;
    }

    public MeasureDTO getMeasures() {
        return measures;
    }

    public void setMeasures(MeasureDTO measures) {
        this.measures = measures;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        TesterDTO testerDTO = (TesterDTO) object;
        return Objects.equals(id, testerDTO.id) && Objects.equals(name, testerDTO.name) &&
                Objects.equals(email, testerDTO.email) && Objects.equals(birthdate, testerDTO.birthdate)
                && sex == testerDTO.sex && Objects.equals(testDone, testerDTO.testDone) &&
                Objects.equals(measures, testerDTO.measures);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, birthdate, sex, testDone, measures);
    }
}
