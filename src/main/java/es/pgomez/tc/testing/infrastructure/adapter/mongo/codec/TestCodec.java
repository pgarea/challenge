package es.pgomez.tc.testing.infrastructure.adapter.mongo.codec;

import com.mongodb.MongoClientSettings;
import es.pgomez.tc.testing.domain.model.Test;
import org.bson.*;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.util.UUID;

public class TestCodec implements CollectibleCodec<Test> {

    private final Codec<Document> documentCodec;

    public TestCodec() {
        this.documentCodec = MongoClientSettings.getDefaultCodecRegistry().get(Document.class);
    }

    @Override
    public void encode(BsonWriter writer, Test test, EncoderContext encoderContext) {
        Document doc = toDocument(test);
        documentCodec.encode(writer, doc, encoderContext);
    }

    @Override
    public Class<Test> getEncoderClass() {
        return Test.class;
    }

    @Override
    public Test generateIdIfAbsentFromDocument(Test document) {
        if (!documentHasId(document)) {
            document.setId(UUID.randomUUID());
        }
        return document;
    }

    @Override
    public boolean documentHasId(Test document) {
        return document.getId() != null;
    }

    @Override
    public BsonValue getDocumentId(Test document) {
        return new BsonString(document.getId().toString());
    }

    @Override
    public Test decode(BsonReader reader, DecoderContext decoderContext) {
        return null;
    }


    private Document toDocument(Test test) {
        Document doc = new Document();
        doc.put("_id", test.getId().toString());
        doc.put("testerId", test.getTester().getId().toString());
        doc.put("size", test.getSize().size());
        doc.put("productId", test.getProduct().getId().toString());
        return doc;
    }
}