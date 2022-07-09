package kitchenpos.menu.validator;

import com.kitchenpos.domain.repository.MenuGroupRepository;
import com.kitchenpos.dto.CreateMenuRequest;
import com.kitchenpos.dto.MenuProductRequest;
import com.kitchenpos.exception.MenuException;
import com.kitchenpos.validator.MenuValidator;
import com.kitchenpos.domain.Product;
import com.kitchenpos.domain.ProductRepository;
import com.kitchenpos.exception.ProductException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MenuValidatorTest {
    @Mock
    ProductRepository productRepository;
    @Mock
    MenuGroupRepository menuGroupRepository;
    @InjectMocks
    MenuValidator menuValidator;

    private Product 데리버거 = Product.of("데리버거", BigDecimal.valueOf(1_000));
    private Product 새우버거 = Product.of("새우버거", BigDecimal.valueOf(1_000));

    @Test
    @DisplayName("정상")
    void validate() {
        // given
        given(menuGroupRepository.existsById(any()))
                .willReturn(true);
        given(productRepository.findAllById(any()))
                .willReturn(Arrays.asList(Product.of("데리버거", BigDecimal.valueOf(1_000)), Product.of("새우버거", BigDecimal.valueOf(1_000))));
        final CreateMenuRequest 햄버거메뉴 = new CreateMenuRequest("햄버거메뉴", BigDecimal.valueOf(2_000), 1L,
                Arrays.asList(new MenuProductRequest(데리버거.getId(), 1), (new MenuProductRequest(새우버거.getId(), 1))));
        // then
        menuValidator.validate(햄버거메뉴);
    }

    @Test
    @DisplayName("메뉴 그룹 조회 안됨")
    void exception_not_found() {
        // given
        given(menuGroupRepository.existsById(any()))
                .willReturn(false);
        final CreateMenuRequest 햄버거메뉴 = new CreateMenuRequest("햄버거메뉴", BigDecimal.valueOf(2_000), 1L,
                Arrays.asList(new MenuProductRequest(데리버거.getId(), 1), (new MenuProductRequest(새우버거.getId(), 1))));
        // then
        assertThatThrownBy(() -> menuValidator.validate(햄버거메뉴))
                .isInstanceOf(MenuException.class);
    }

    @Test
    @DisplayName("상품 조회 개수 안맞음")
    void exception_count() {
        // given
        given(menuGroupRepository.existsById(any()))
                .willReturn(true);
        given(productRepository.findAllById(any()))
                .willReturn(Arrays.asList(Product.of("데리버거", BigDecimal.valueOf(1_000))));
        final CreateMenuRequest 햄버거메뉴 = new CreateMenuRequest("햄버거메뉴", BigDecimal.valueOf(2_000), 1L,
                Arrays.asList(new MenuProductRequest(데리버거.getId(), 1), (new MenuProductRequest(새우버거.getId(), 1))));
        // then
        assertThatThrownBy(() ->menuValidator.validate(햄버거메뉴))
                .isInstanceOf(ProductException.class);
    }

    @Test
    @DisplayName("상품 의 합보다 많은 금액")
    void exception() {
        // given
        given(menuGroupRepository.existsById(any()))
                .willReturn(true);
        given(productRepository.findAllById(any()))
                .willReturn(Arrays.asList(Product.of("데리버거", BigDecimal.valueOf(1_000)), Product.of("새우버거", BigDecimal.valueOf(1_000))));
        final CreateMenuRequest 햄버거메뉴 = new CreateMenuRequest("햄버거메뉴", BigDecimal.valueOf(5_000), 1L,
                Arrays.asList(new MenuProductRequest(데리버거.getId(), 1), (new MenuProductRequest(새우버거.getId(), 1))));
        // then
        assertThatThrownBy(() ->menuValidator.validate(햄버거메뉴))
                .isInstanceOf(MenuException.class);
    }
}
