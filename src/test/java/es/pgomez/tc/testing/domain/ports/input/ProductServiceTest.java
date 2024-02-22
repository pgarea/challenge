package es.pgomez.tc.testing.domain.ports.input;

import es.pgomez.tc.testing.domain.model.Product;
import es.pgomez.tc.testing.domain.ports.dto.PageableDTO;
import es.pgomez.tc.testing.domain.ports.dto.ProductCreationDTO;
import es.pgomez.tc.testing.domain.ports.dto.ProductDTO;
import es.pgomez.tc.testing.domain.ports.input.interfaces.ProductService;
import es.pgomez.tc.testing.domain.ports.output.ProductRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.UUID;

@QuarkusTest
class ProductServiceTest {

    @InjectMock
    ProductRepository productRepository;

    @Inject
    ProductService productService;

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

    private ProductCreationDTO getProductCreation() {
        ProductCreationDTO productCreationDTO = new ProductCreationDTO();
        productCreationDTO.setSku("skuTest");
        productCreationDTO.setSizes(List.of("L"));
        productCreationDTO.setPictures(List.of("http://pictures.com"));
        productCreationDTO.setBrandName("brandNameTest");
        productCreationDTO.setBrandLogo("http://logo.com");
        productCreationDTO.setColor("red");
        return productCreationDTO;
    }

    @Test
    void getByIdTest() throws InstanceNotFoundException {
        Product product = getProduct();
        Mockito.when(productRepository.getById(product.getId())).thenReturn(product);
        ProductDTO found = productService.getById(product.getId());
        Assertions.assertEquals(product.getId(), found.getId());
    }

    @Test
    void getByNonExistingIdTest() throws InstanceNotFoundException {
        Product product = getProduct();
        Mockito.when(productRepository.getById(product.getId())).thenThrow(new InstanceNotFoundException());
        Assertions.assertThrows(InstanceNotFoundException.class, () ->
                productService.getById(product.getId()));
    }

    @Test
    void getAllTest() {
        Product product = getProduct();
        List<ProductDTO> productDTOList = List.of(new ProductDTO(product));
        PageableDTO<ProductDTO> pageableDTO = new PageableDTO<>(productDTOList, 1L);
        Mockito.when(productRepository.getAll(null, null)).thenReturn(List.of(product));
        Mockito.when(productRepository.count()).thenReturn(1L);

        PageableDTO<ProductDTO> products = productService.getAll(null, null);
        Assertions.assertEquals(pageableDTO.getCount(), products.getCount());
        Assertions.assertEquals(pageableDTO.getValues(), products.getValues());
    }

    @Test
    void createProductTest() throws ValidationException {
        Product product = getProduct();
        ProductCreationDTO productCreationDTO = getProductCreation();
        Mockito.when(productRepository.createProduct(Mockito.any(Product.class))).thenReturn(product);

        ProductDTO createdProduct = productService.createProduct(productCreationDTO);
        Assertions.assertEquals(createdProduct.getId(), product.getId());
    }

    @Test
    void createInvalidProductTest() throws ValidationException {
        ProductCreationDTO productCreationDTO = getProductCreation();
        Mockito.when(productRepository.createProduct(Mockito.any(Product.class))).thenThrow(new ValidationException());

        Assertions.assertThrows(ValidationException.class, () ->
                productService.createProduct(productCreationDTO));
    }

    @Test
    void updateProductTest() throws ValidationException, InstanceNotFoundException {
        ProductCreationDTO productCreationDTO = getProductCreation();
        Product product = getProduct();
        Mockito.when(productRepository.updateProduct(Mockito.any(UUID.class), Mockito.any(Product.class)))
                .thenReturn(product);

        ProductDTO updatedProduct = productService.updateProduct(product.getId(), productCreationDTO);
        Assertions.assertEquals(product.getId(), updatedProduct.getId());
        Assertions.assertEquals(product.getBrand().getName().name(), updatedProduct.getBrand().getName());
    }

    @Test
    void updateNonExistentProductTest() throws ValidationException, InstanceNotFoundException {
        ProductCreationDTO productCreationDTO = getProductCreation();
        UUID id = UUID.randomUUID();
        Mockito.when(productRepository.updateProduct(Mockito.any(UUID.class), Mockito.any(Product.class)))
                .thenThrow(new InstanceNotFoundException());

        Assertions.assertThrows(InstanceNotFoundException.class, () ->
                productService.updateProduct(id, productCreationDTO));

    }

    @Test
    void deleteProductTest() throws InstanceNotFoundException {
        UUID uuid = UUID.randomUUID();
        Mockito.doNothing().when(productRepository).deleteProduct(uuid);
        productService.deleteProduct(uuid);
        Assertions.assertTrue(true);
    }
}
