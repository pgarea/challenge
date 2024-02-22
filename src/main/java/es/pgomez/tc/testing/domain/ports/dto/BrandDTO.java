package es.pgomez.tc.testing.domain.ports.dto;

import es.pgomez.tc.testing.domain.model.Brand;

import java.util.Objects;

public class BrandDTO {

    private String name;

    private String logo;

    public BrandDTO() {
    }

    public BrandDTO(Brand brand) {
        this.name = brand.getName().name();
        this.logo = brand.getLogo().logo();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public String toString() {
        return "BrandDTO{" +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        BrandDTO brandDTO = (BrandDTO) object;
        return Objects.equals(name, brandDTO.name) && Objects.equals(logo, brandDTO.logo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, logo);
    }
}
