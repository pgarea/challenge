package es.pgomez.tc.testing.infrastructure.adapter.mongo.codec;

import com.mongodb.MongoClientSettings;
import es.pgomez.tc.testing.domain.model.Product;
import es.pgomez.tc.testing.domain.model.common.SizeType;
import org.bson.*;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.util.List;
import java.util.UUID;

public class ProductCodec implements CollectibleCodec<Product> {

    private final Codec<Document> documentCodec;

    public ProductCodec() {
        this.documentCodec = MongoClientSettings.getDefaultCodecRegistry().get(Document.class);
    }

    @Override
    public void encode(BsonWriter writer, Product product, EncoderContext encoderContext) {
        documentCodec.encode(writer, toDocument(product), encoderContext);
    }

    @Override
    public Class<Product> getEncoderClass() {
        return Product.class;
    }

    @Override
    public Product generateIdIfAbsentFromDocument(Product document) {
        if (!documentHasId(document)) {
            document.setId(UUID.randomUUID());
        }
        return document;
    }

    @Override
    public boolean documentHasId(Product document) {
        return document.getId() != null;
    }

    @Override
    public BsonValue getDocumentId(Product document) {
        return new BsonString(document.getId().toString());
    }

    @Override
    public Product decode(BsonReader reader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(reader, decoderContext);
        return toObject(document);
    }

    private Product toObject(Document doc) {
        UUID id = UUID.fromString(doc.getString("_id"));
        String sku = doc.getString("sku");
        List<String> sizes = doc.getList("sizes", String.class);
        List<String> pictures = doc.getList("pictures", String.class);
        String brandName = doc.getString("brandName");
        String brandLogo = doc.getString("brandLogo");
        String color = doc.getString("color");

        return new Product(id, sku, sizes, pictures, brandName, brandLogo, color);
    }

    private Document toDocument(Product product) {
        Document doc = new Document();
        doc.put("_id", product.getId().toString());
        doc.put("sku", product.getSku().sku());
        doc.put("sizes", product.getSizes().sizes().stream().map(SizeType::size).toList());
        doc.put("pictures", product.getPictures().pictures());
        doc.put("brandName", product.getBrand().getName().name());
        doc.put("brandLogo", product.getBrand().getLogo().logo());
        doc.put("color", product.getColor().color());
        return doc;
    }
}
