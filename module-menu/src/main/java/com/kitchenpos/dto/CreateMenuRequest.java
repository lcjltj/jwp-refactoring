package com.kitchenpos.dto;

import com.kitchenpos.domain.Menu;
import com.kitchenpos.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CreateMenuRequest {
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductRequest> menuProducts;

    public CreateMenuRequest(final String name, final BigDecimal price, final Long menuGroupId, final List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu toEntity() {
        return Menu.of(name, price, menuGroupId);
    }

    public List<MenuProduct> toMenuProducts() {
        return Collections.unmodifiableList(
                menuProducts.stream()
                .map(MenuProductRequest::toEntity)
                .collect(Collectors.toList())
        );
    }



    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    @Override
    public String toString() {
        return "CreateMenuRequest{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", menuGroupId=" + menuGroupId +
                ", menuProducts=" + menuProducts +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CreateMenuRequest that = (CreateMenuRequest) o;
        return Objects.equals(name, that.name) && Objects.equals(price, that.price) && Objects.equals(menuGroupId, that.menuGroupId) && Objects.equals(menuProducts, that.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, menuGroupId, menuProducts);
    }
}
