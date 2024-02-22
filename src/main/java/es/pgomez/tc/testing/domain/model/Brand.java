package es.pgomez.tc.testing.domain.model;

import es.pgomez.tc.testing.domain.model.common.LogoType;
import es.pgomez.tc.testing.domain.model.common.NameType;

import java.util.Objects;

public class Brand {
    private NameType name;
    private LogoType logo;

    public Brand(String name, String logo) {
        this.name = new NameType(name);
        this.logo = new LogoType(logo);
    }

    public NameType getName() {
        return name;
    }

    public LogoType getLogo() {
        return logo;
    }

    @Override
    public String toString() {
        return "Brand{" +
                ", name=" + name +
                ", logo=" + logo +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Brand brand = (Brand) object;
        return Objects.equals(name, brand.name) && Objects.equals(logo, brand.logo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, logo);
    }
}
