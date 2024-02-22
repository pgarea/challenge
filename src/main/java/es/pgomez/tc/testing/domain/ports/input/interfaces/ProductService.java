package es.pgomez.tc.testing.domain.ports.input.interfaces;

import es.pgomez.tc.testing.domain.ports.dto.PageableDTO;
import es.pgomez.tc.testing.domain.ports.dto.ProductCreationDTO;
import es.pgomez.tc.testing.domain.ports.dto.ProductDTO;
import jakarta.validation.ValidationException;

import javax.management.InstanceNotFoundException;
import java.util.UUID;

public interface ProductService {

    ProductDTO getById(UUID id) throws InstanceNotFoundException;

    PageableDTO<ProductDTO> getAll(Long page, Long size);

    ProductDTO createProduct(ProductCreationDTO product) throws ValidationException;

    ProductDTO updateProduct(UUID id, ProductCreationDTO product) throws ValidationException, InstanceNotFoundException;

    void deleteProduct(UUID id) throws InstanceNotFoundException;
}
