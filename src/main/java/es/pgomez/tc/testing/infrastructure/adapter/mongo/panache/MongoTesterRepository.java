package es.pgomez.tc.testing.infrastructure.adapter.mongo.panache;

import es.pgomez.tc.testing.domain.model.Tester;
import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MongoTesterRepository implements PanacheMongoRepositoryBase<Tester, String> {
}
