package es.pgomez.tc.testing.infrastructure.adapter.mongo.repository;

import es.pgomez.tc.testing.domain.model.Product;
import es.pgomez.tc.testing.domain.ports.output.ProductRepository;
import es.pgomez.tc.testing.infrastructure.adapter.mongo.panache.MongoProductRepository;
import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ProductRepositoryMongoImpl implements ProductRepository {

    private final MongoProductRepository mongoProductRepository;

    public ProductRepositoryMongoImpl(MongoProductRepository mongoProductRepository) {
        this.mongoProductRepository = mongoProductRepository;
    }

    @Override
    public Product getById(UUID id) throws InstanceNotFoundException {
        Optional<Product> productOptional = mongoProductRepository.findByIdOptional(id.toString());
        if (productOptional.isPresent()) {
            return productOptional.get();
        }
        throw new InstanceNotFoundException();
    }

    @Override
    public List<Product> getAll(Long page, Long size) {
        PanacheQuery<Product> query = mongoProductRepository.findAll();
        if (page != null && size != null) {
            query = query.page(new Page(page.intValue(), size.intValue()));
        }
        return query.stream().toList();
    }

    @Override
    public Long count() {
        return mongoProductRepository.count();
    }

    @Override
    public Product createProduct(Product product) {
        mongoProductRepository.persist(product);
        return product;
    }

    @Override
    public Product updateProduct(UUID id, Product product) throws InstanceNotFoundException {
        Optional<Product> productOptional = mongoProductRepository.findByIdOptional(id.toString());
        if (productOptional.isPresent()) {
            mongoProductRepository.update(product);
            return product;
        }
        throw new InstanceNotFoundException();
    }

    @Override
    public void deleteProduct(UUID id) throws InstanceNotFoundException {
        boolean deletedElements = mongoProductRepository.deleteById(id.toString());
        if (!deletedElements) {
            throw new InstanceNotFoundException();
        }
    }
}
