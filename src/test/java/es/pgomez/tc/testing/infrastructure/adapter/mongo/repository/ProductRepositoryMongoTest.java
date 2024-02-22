package es.pgomez.tc.testing.infrastructure.adapter.mongo.repository;

import es.pgomez.tc.testing.domain.model.Product;
import es.pgomez.tc.testing.domain.ports.output.ProductRepository;
import es.pgomez.tc.testing.infrastructure.adapter.mongo.panache.MongoProductRepository;
import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@QuarkusTest
class ProductRepositoryMongoTest {

    @InjectMock
    MongoProductRepository mongoProductRepository;

    @Inject
    ProductRepository productRepository;

    private Product getProduct() {
        UUID id = UUID.randomUUID();
        String sku = "skuTest";
        List<String> sizes = List.of("L");
        List<String> pictures = List.of("http://pictures.com");
        String brandName = "brandNameTest";
        String brandLogo = "http://logo.com";
        String color = "red";
        return new Product(id, sku, sizes, pictures, brandName, brandLogo, color);
    }

    @Test
    void getByIdTest() throws InstanceNotFoundException {
        UUID id = UUID.randomUUID();
        Product product = getProduct();
        Optional<Product> productOptional = Optional.of(product);
        Mockito.when(mongoProductRepository.findByIdOptional(id.toString())).thenReturn(productOptional);

        Product foundProduct = productRepository.getById(id);
        Assertions.assertEquals(product, foundProduct);
    }

    @Test
    void getByNonExitentIdTest() throws InstanceNotFoundException {
        UUID id = UUID.randomUUID();
        Optional<Product> productOptional = Optional.empty();
        Mockito.when(mongoProductRepository.findByIdOptional(id.toString())).thenReturn(productOptional);

        Assertions.assertThrows(InstanceNotFoundException.class, () ->
                productRepository.getById(id));
    }

    @Test
    void getAllTest() {
        PanacheQuery<Product> query = Mockito.mock(PanacheQuery.class);
        Mockito.when(mongoProductRepository.findAll()).thenReturn(query);
        Mockito.when(query.stream()).thenReturn(Stream.of(getProduct()));

        List<Product> products = productRepository.getAll(null, null);
        Assertions.assertEquals(1, products.size());
    }

    @Test
    void countTest() {
        Mockito.when(mongoProductRepository.count()).thenReturn(4L);

        Long count = productRepository.count();
        Assertions.assertEquals(4l, count);
    }

    @Test
    void createProductTest() {
        Product product = getProduct();
        Mockito.doNothing().when(mongoProductRepository).persist(product);

        productRepository.createProduct(product);
        Assertions.assertTrue(true);
    }

    @Test
    void updateProductTest() throws InstanceNotFoundException {
        UUID id = UUID.randomUUID();
        Product product = getProduct();
        Optional<Product> productOptional = Optional.of(product);
        Mockito.when(mongoProductRepository.findByIdOptional(id.toString())).thenReturn(productOptional);
        Mockito.doNothing().when(mongoProductRepository).update(product);

        productRepository.updateProduct(id, product);
        Assertions.assertTrue(true);
    }

    @Test
    void updateNonExistentProduct() throws InstanceNotFoundException {
        UUID id = UUID.randomUUID();
        Product product = getProduct();
        Optional<Product> productOptional = Optional.empty();
        Mockito.when(mongoProductRepository.findByIdOptional(id.toString())).thenReturn(productOptional);

        Assertions.assertThrows(InstanceNotFoundException.class, () ->
                productRepository.updateProduct(id, product));
    }

    @Test
    void deleteProductTest() throws InstanceNotFoundException {
        UUID id = UUID.randomUUID();
        Mockito.when(mongoProductRepository.deleteById(id.toString())).thenReturn(true);

        productRepository.deleteProduct(id);
        Assertions.assertTrue(true);
    }

    @Test
    void deleteNonExistentProductTest() {
        UUID id = UUID.randomUUID();
        Mockito.when(mongoProductRepository.deleteById(id.toString())).thenReturn(false);

        Assertions.assertThrows(InstanceNotFoundException.class, () ->
                productRepository.deleteProduct(id));

    }
}
