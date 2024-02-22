package es.pgomez.tc.testing.domain.ports.dto;

import es.pgomez.tc.testing.domain.model.common.util.Sex;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class TesterCreationDTO {

    private String name;

    private String email;

    private String password;

    private LocalDate birthdate;

    private Sex sex;

    private LocalDateTime measuresCreationDate;

    private Double heightCm;

    private Double weightKg;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public LocalDateTime getMeasuresCreationDate() {
        return measuresCreationDate;
    }

    public void setMeasuresCreationDate(LocalDateTime measuresCreationDate) {
        this.measuresCreationDate = measuresCreationDate;
    }

    public Double getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(Double heightCm) {
        this.heightCm = heightCm;
    }

    public Double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(Double weightKg) {
        this.weightKg = weightKg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TesterCreationDTO that = (TesterCreationDTO) o;
        return Objects.equals(name, that.name) && Objects.equals(email, that.email) && Objects.equals(password, that.password) && Objects.equals(birthdate, that.birthdate) && sex == that.sex && Objects.equals(measuresCreationDate, that.measuresCreationDate) && Objects.equals(heightCm, that.heightCm) && Objects.equals(weightKg, that.weightKg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, password, birthdate, sex, measuresCreationDate, heightCm, weightKg);
    }
}
