package es.pgomez.tc.testing.infrastructure.adapter.mongo.panache;

import es.pgomez.tc.testing.domain.model.Test;
import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MongoTestRepository implements PanacheMongoRepositoryBase<Test, String> {
}
