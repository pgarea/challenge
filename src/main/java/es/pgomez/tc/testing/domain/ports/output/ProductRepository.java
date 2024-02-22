package es.pgomez.tc.testing.domain.ports.output;

import es.pgomez.tc.testing.domain.model.Product;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.UUID;

public interface ProductRepository {

    Product getById(UUID id) throws InstanceNotFoundException;

    List<Product> getAll(Long page, Long size);

    Long count();

    Product createProduct(Product product);

    Product updateProduct(UUID id, Product product) throws InstanceNotFoundException;

    void deleteProduct(UUID id) throws InstanceNotFoundException;
}
