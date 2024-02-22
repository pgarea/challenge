package es.pgomez.tc.testing.domain.ports.input;

import es.pgomez.tc.testing.domain.model.Product;
import es.pgomez.tc.testing.domain.ports.dto.PageableDTO;
import es.pgomez.tc.testing.domain.ports.dto.ProductCreationDTO;
import es.pgomez.tc.testing.domain.ports.dto.ProductDTO;
import es.pgomez.tc.testing.domain.ports.input.interfaces.ProductService;
import es.pgomez.tc.testing.domain.ports.output.ProductRepository;
import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ValidationException;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ProductServiceImpl implements ProductService {

    private final EventBus bus;

    private final ProductRepository productRepository;

    public ProductServiceImpl(EventBus bus, ProductRepository productRepository) {
        this.bus = bus;
        this.productRepository = productRepository;
    }

    @Override
    public ProductDTO getById(UUID id) throws InstanceNotFoundException {
        Product product = productRepository.getById(id);
        return new ProductDTO(product);
    }

    @Override
    public PageableDTO<ProductDTO> getAll(Long page, Long size) {
        List<Product> products = productRepository.getAll(page, size);
        Long count = productRepository.count();
        return new PageableDTO<>(products.stream().map(ProductDTO::new).toList(), count);
    }

    @Override
    public ProductDTO createProduct(ProductCreationDTO productCreationDTO) throws ValidationException {
        Product newProduct = new Product(null, productCreationDTO.getSku(), productCreationDTO.getSizes(),
                productCreationDTO.getPictures(), productCreationDTO.getBrandName(), productCreationDTO.getBrandLogo(),
                productCreationDTO.getColor());

        return new ProductDTO(productRepository.createProduct(newProduct));
    }

    @Override
    public ProductDTO updateProduct(UUID id, ProductCreationDTO productCreationDTO) throws ValidationException, InstanceNotFoundException {
        Product productToUpdate = new Product(id, productCreationDTO.getSku(), productCreationDTO.getSizes(),
                productCreationDTO.getPictures(), productCreationDTO.getBrandName(), productCreationDTO.getBrandLogo(),
                productCreationDTO.getColor());

        return new ProductDTO(productRepository.updateProduct(id, productToUpdate));
    }

    @Override
    public void deleteProduct(UUID id) throws InstanceNotFoundException {
        productRepository.deleteProduct(id);
        bus.publish("DELETED_PRODUCT", id);
    }

}
