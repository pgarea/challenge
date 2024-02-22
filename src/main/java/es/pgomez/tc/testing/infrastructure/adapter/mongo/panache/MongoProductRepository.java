package es.pgomez.tc.testing.infrastructure.adapter.mongo.panache;

import es.pgomez.tc.testing.domain.model.Product;
import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MongoProductRepository implements PanacheMongoRepositoryBase<Product, String> {


}
