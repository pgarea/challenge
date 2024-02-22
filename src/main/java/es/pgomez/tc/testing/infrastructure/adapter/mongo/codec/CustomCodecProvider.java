package es.pgomez.tc.testing.infrastructure.adapter.mongo.codec;

import es.pgomez.tc.testing.domain.model.Product;
import es.pgomez.tc.testing.domain.model.Test;
import es.pgomez.tc.testing.domain.model.Tester;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class CustomCodecProvider implements CodecProvider {

    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz.equals(Product.class)) {
            return (Codec<T>) new ProductCodec();
        } else if (clazz.equals(Tester.class)) {
            return (Codec<T>) new TesterCodec();
        } else if (clazz.equals(Test.class)) {
            return (Codec<T>) new TestCodec();
        }
        return null;
    }

}