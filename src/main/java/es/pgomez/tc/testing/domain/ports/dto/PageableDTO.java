package es.pgomez.tc.testing.domain.ports.dto;

import java.util.List;
import java.util.Objects;

public class PageableDTO<T> {

    private List<T> values;

    private Long count;

    public PageableDTO(List<T> t, Long count) {
        this.values = t;
        this.count = count;
    }

    public List<T> getValues() {
        return values;
    }

    public void setValues(List<T> values) {
        this.values = values;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PageableDTO<?> that = (PageableDTO<?>) object;
        return Objects.equals(values, that.values) && Objects.equals(count, that.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values, count);
    }
}
