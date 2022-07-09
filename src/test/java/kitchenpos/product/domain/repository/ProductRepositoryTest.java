package kitchenpos.product.domain.repository;

import com.kitchenpos.domain.Product;
import com.kitchenpos.domain.ProductRepository;
import com.kitchenpos.dto.CreateProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProductRepositoryTest {
    public static final CreateProductRequest 불고기버거 = new CreateProductRequest("불고기버거", 1500);
    public static final CreateProductRequest 새우버거 = new CreateProductRequest("새우버거", 2000);
    @Autowired
    ProductRepository productRepository;

    @Test
    @DisplayName("제품 생성")
    void create() {
        // when
        final Product product = productRepository.save(불고기버거.toEntity());
        // then
        assertThat(product.getName()).isEqualTo("불고기버거");
        assertThat(product.getPrice()).isEqualTo(BigDecimal.valueOf(1500));
    }
}
