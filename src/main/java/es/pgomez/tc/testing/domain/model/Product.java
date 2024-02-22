package es.pgomez.tc.testing.domain.model;

import es.pgomez.tc.testing.domain.model.common.ColorType;
import es.pgomez.tc.testing.domain.model.common.PicturesType;
import es.pgomez.tc.testing.domain.model.common.SKUType;
import es.pgomez.tc.testing.domain.model.common.SizesType;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Product {

    private UUID id;

    private final SKUType sku;

    private final SizesType sizes;

    private final PicturesType pictures;

    private final Brand brand;

    private final ColorType color;


    public Product(UUID id, String sku, List<String> sizes, List<String> pictures, String brandName,
                   String brandLogo, String color) {
        this.id = id;
        this.sku = new SKUType(sku);
        this.sizes = new SizesType(sizes);
        this.pictures = new PicturesType(pictures);
        this.brand = new Brand(brandName, brandLogo);
        this.color = new ColorType(color);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public SKUType getSku() {
        return sku;
    }

    public SizesType getSizes() {
        return sizes;
    }

    public PicturesType getPictures() {
        return pictures;
    }

    public Brand getBrand() {
        return brand;
    }

    public ColorType getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sku, sizes, pictures, brand, color);
    }
}
