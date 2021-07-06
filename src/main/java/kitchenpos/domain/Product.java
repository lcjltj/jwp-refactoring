package kitchenpos.domain;

import static java.util.Objects.*;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Product {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @AttributeOverride(name = "value", column = @Column(name = "name", nullable = false))
    private Name name;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "price", nullable = false))
    private Price price;

    protected Product() {}

    public Product(String name, BigDecimal price) {
        this(Name.valueOf(name), Price.wonOf(price));
    }

    Product(Name name, Price price) {
        validateNonNull(name, price);
        this.name = name;
        this.price = price;
    }

    private void validateNonNull(Name name, Price price) {
        if (isNull(name) || isNull(price)) {
            throw new IllegalArgumentException("상품의 이름과 가격은 필수 정보입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public BigDecimal getPriceAmount() {
        return price.getAmount();
    }
}