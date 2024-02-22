package es.pgomez.tc.testing.domain.model;

import es.pgomez.tc.testing.domain.model.common.CreationDateType;
import es.pgomez.tc.testing.domain.model.common.HeightType;
import es.pgomez.tc.testing.domain.model.common.WeightType;

import java.time.LocalDateTime;
import java.util.Objects;

public class Measure {

    private CreationDateType creationDate;

    private HeightType heightCm;

    private WeightType weightKg;

    public Measure(LocalDateTime localDateTime, Double heightCm, Double weightKg) {
        if (localDateTime == null) {
            this.creationDate = new CreationDateType(localDateTime);
        } else {
            this.creationDate = new CreationDateType(LocalDateTime.now());
        }
        this.heightCm = new HeightType(heightCm);
        this.weightKg = new WeightType(weightKg);
    }

    public CreationDateType getCreationDate() {
        return creationDate;
    }

    public HeightType getHeightCm() {
        return heightCm;
    }

    public WeightType getWeightKg() {
        return weightKg;
    }

    @Override
    public String toString() {
        return "Measure{" +
                ", creationDate=" + creationDate +
                ", heightCm=" + heightCm +
                ", weightKg=" + weightKg +
                '}';
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Measure measure = (Measure) object;
        return Objects.equals(creationDate, measure.creationDate) && Objects.equals(heightCm, measure.heightCm) && Objects.equals(weightKg, measure.weightKg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creationDate, heightCm, weightKg);
    }
}
