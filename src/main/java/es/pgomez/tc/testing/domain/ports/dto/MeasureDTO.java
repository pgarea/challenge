package es.pgomez.tc.testing.domain.ports.dto;

import es.pgomez.tc.testing.domain.model.Measure;

import java.time.LocalDateTime;
import java.util.Objects;

public class MeasureDTO {

    private LocalDateTime creationDate;

    private Double heightCm;

    private Double weightKg;

    public MeasureDTO() {
    }

    public MeasureDTO(Measure measure) {
        this.creationDate = measure.getCreationDate().creationDate();
        this.heightCm = measure.getHeightCm().height();
        this.weightKg = measure.getWeightKg().weight();
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
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
    public String toString() {
        return "MeasureDTO{" +
                "creationDate=" + creationDate +
                ", heightCm=" + heightCm +
                ", weightKg=" + weightKg +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        MeasureDTO that = (MeasureDTO) object;
        return Objects.equals(creationDate, that.creationDate) && Objects.equals(heightCm, that.heightCm) && Objects.equals(weightKg, that.weightKg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creationDate, heightCm, weightKg);
    }
}
