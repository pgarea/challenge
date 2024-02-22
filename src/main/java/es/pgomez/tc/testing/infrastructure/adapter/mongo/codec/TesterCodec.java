package es.pgomez.tc.testing.infrastructure.adapter.mongo.codec;

import com.mongodb.MongoClientSettings;
import es.pgomez.tc.testing.domain.model.Tester;
import es.pgomez.tc.testing.domain.model.common.util.Sex;
import org.bson.*;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

public class TesterCodec implements CollectibleCodec<Tester> {

    private final Codec<Document> documentCodec;

    public TesterCodec() {
        this.documentCodec = MongoClientSettings.getDefaultCodecRegistry().get(Document.class);
    }

    @Override
    public void encode(BsonWriter writer, Tester tester, EncoderContext encoderContext) {
        documentCodec.encode(writer, toDocument(tester), encoderContext);
    }

    @Override
    public Class<Tester> getEncoderClass() {
        return Tester.class;
    }

    @Override
    public Tester generateIdIfAbsentFromDocument(Tester document) {
        if (!documentHasId(document)) {
            document.setId(UUID.randomUUID());
        }
        return document;
    }

    @Override
    public boolean documentHasId(Tester document) {
        return document.getId() != null;
    }

    @Override
    public BsonValue getDocumentId(Tester document) {
        return new BsonString(document.getId().toString());
    }

    @Override
    public Tester decode(BsonReader reader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(reader, decoderContext);
        return toObject(document);
    }

    private Tester toObject(Document doc) {
        UUID id = UUID.fromString(doc.getString("_id"));
        String name = doc.getString("name");
        String email = doc.getString("email");
        String password = doc.getString("password");
        LocalDate birthdate = doc.get("birthdate", Date.class).toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        Sex sex = Sex.valueOf(doc.getString("sex"));
        Long testDone = doc.getLong("testDone");
        Document measures = doc.get("measures", Document.class);
        LocalDateTime creationDate = measures.get("creationDate", Date.class).toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        Double height = measures.get("heightCm", Double.class);
        Double weight = measures.get("weightKg", Double.class);
        Tester tester = new Tester(id, name, email, birthdate, sex, testDone,
                creationDate, height, weight);
        tester.setRawPassword(password);
        return tester;
    }

    private Document toDocument(Tester tester) {
        Document doc = new Document();
        doc.put("_id", tester.getId().toString());
        doc.put("name", tester.getName().name());
        doc.put("email", tester.getEmail().email());
        doc.put("password", tester.getPassword().password());
        doc.put("birthdate", tester.getBirthdate().birthdate());
        doc.put("sex", tester.getSex().sex().name());
        doc.put("testDone", tester.getTestDone().testDone());
        Document measures = new Document();
        measures.put("creationDate", tester.getMeasures().getCreationDate().creationDate());
        measures.put("heightCm", tester.getMeasures().getHeightCm().height());
        measures.put("weightKg", tester.getMeasures().getWeightKg().weight());
        doc.put("measures", measures);
        return doc;
    }
}
