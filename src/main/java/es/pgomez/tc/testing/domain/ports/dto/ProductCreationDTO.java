package es.pgomez.tc.testing.domain.ports.dto;

import java.util.List;
import java.util.Objects;

public class ProductCreationDTO {

    private String sku;

    private List<String> sizes;

    private List<String> pictures;

    private String brandName;

    private String brandLogo;

    private String color;


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

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandLogo() {
        return brandLogo;
    }

    public void setBrandLogo(String brandLogo) {
        this.brandLogo = brandLogo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCreationDTO that = (ProductCreationDTO) o;
        return Objects.equals(sku, that.sku) && Objects.equals(sizes, that.sizes) &&
                Objects.equals(pictures, that.pictures) && Objects.equals(brandName, that.brandName) &&
                Objects.equals(brandLogo, that.brandLogo) && Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sku, sizes, pictures, brandName, brandLogo, color);
    }
}
