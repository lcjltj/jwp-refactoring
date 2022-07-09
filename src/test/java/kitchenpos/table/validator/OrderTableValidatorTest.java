package kitchenpos.table.validator;

import com.kitchenpos.validator.OrderTableValidator;
import com.kitchenpos.domain.Order;
import com.kitchenpos.domain.repository.OrderRepository;
import com.kitchenpos.domain.OrderStatus;
import com.kitchenpos.domain.OrderTable;
import com.kitchenpos.exception.TableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OrderTableValidatorTest {
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderTableValidator validator;

    @Test
    @DisplayName("요리 중 테이블 분리 청소 오류")
    void validateTableSeparate() {
        // given
        given(orderRepository.findByOrderTableId(any()))
                .willReturn(Optional.of(new Order(1L, 1L, OrderStatus.COOKING)));
        // then
        assertThatThrownBy(() ->
                validator.validateTableSeparate(Arrays.asList(OrderTable.of(0, true))))
                .isInstanceOf(TableException.class);
    }

    @Test
    @DisplayName("테이블 분리 청소")
    void tableSeparateSuccess() {
        // given
        given(orderRepository.findByOrderTableId(any()))
                .willReturn(Optional.of(new Order(1L, 1L, OrderStatus.COMPLETION)));
        // then
        validator.validateTableSeparate(Arrays.asList(OrderTable.of(0, true)));
    }

}
