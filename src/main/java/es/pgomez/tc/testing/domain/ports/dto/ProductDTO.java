package es.pgomez.tc.testing.domain.ports.dto;

import es.pgomez.tc.testing.domain.model.Product;
import es.pgomez.tc.testing.domain.model.common.SizeType;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ProductDTO {

    private UUID id;

    private String sku;

    private List<String> sizes;

    private List<String> pictures;

    private BrandDTO brand;

    private String color;
    
    public ProductDTO() {
    }

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.sku = product.getSku().sku();
        this.sizes = product.getSizes().sizes().stream().map(SizeType::size).toList();
        this.pictures = product.getPictures().pictures();
        this.brand = new BrandDTO(product.getBrand());
        this.color = product.getColor().color();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public List<String> getSizes() {
        return sizes;
    }

    public void setSizes(List<String> sizes) {
        this.sizes = sizes;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public BrandDTO getBrand() {
        return brand;
    }

    public void setBrand(BrandDTO brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + id +
                ", sku='" + sku + '\'' +
                ", sizes=" + sizes +
                ", pictures=" + pictures +
                ", brand=" + brand +
                ", color='" + color + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ProductDTO that = (ProductDTO) object;
        return Objects.equals(id, that.id) && Objects.equals(sku, that.sku) && Objects.equals(sizes, that.sizes) &&
                Objects.equals(pictures, that.pictures) && Objects.equals(brand, that.brand) &&
                Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sku, sizes, pictures, brand, color);
    }
}
