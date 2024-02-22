package es.pgomez.tc.testing.domain.model;

import es.pgomez.tc.testing.domain.model.common.SizeType;
import jakarta.validation.ValidationException;

import java.util.Objects;
import java.util.UUID;

public class Test {

    private static final String VALIDATION_ERR_MSG = "INVALID_SIZE";

    private UUID id;

    private final Tester tester;

    private final Product product;

    private final SizeType size;


    public Test(Tester tester, Product product, String size) {
        this.id = UUID.randomUUID();
        this.tester = tester;
        this.product = product;
        this.size = new SizeType(size);
        if (!product.getSizes().sizes().contains(this.size)) {
            throw new ValidationException(VALIDATION_ERR_MSG);
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Tester getTester() {
        return tester;
    }

    public Product getProduct() {
        return product;
    }

    public SizeType getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", tester=" + tester +
                ", product=" + product +
                ", size=" + size +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Test test = (Test) o;
        return Objects.equals(id, test.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tester, product, size);
    }
}
