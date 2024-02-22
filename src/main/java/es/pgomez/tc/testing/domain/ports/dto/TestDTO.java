package es.pgomez.tc.testing.domain.ports.dto;


import es.pgomez.tc.testing.domain.model.Test;

import java.util.Objects;
import java.util.UUID;

public class TestDTO {

    private UUID productId;

    private String size;

    public TestDTO() {
    }

    public TestDTO(Test test) {
        this.productId = test.getProduct().getId();
        this.size = test.getSize().size();
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestDTO testDTO = (TestDTO) o;
        return Objects.equals(productId, testDTO.productId) && Objects.equals(size, testDTO.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, size);
    }
}
