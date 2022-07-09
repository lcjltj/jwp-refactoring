package kitchenpos.table.application;

import com.kitchenpos.application.TableService;
import com.kitchenpos.domain.OrderTable;
import com.kitchenpos.domain.repository.OrderTableRepository;
import com.kitchenpos.dto.OrderTableResponse;
import com.kitchenpos.validator.OrderTableValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    public static final OrderTable 일번_테이블 = OrderTable.of(0, true);
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrderTableValidator orderTableValidator;
    @InjectMocks
    TableService tableService;

    @AfterEach
    void afterEach() {
    }

    @Test
    @DisplayName("테이블 추가")
    void create() {
        // given
        given(orderTableRepository.save((any())))
                .willReturn(일번_테이블);
        // when
        OrderTableResponse orderTableResponse = tableService.create(일번_테이블);
        // then
        assertThat(orderTableResponse).isInstanceOf(OrderTableResponse.class);
    }

    @Test
    @DisplayName("테이블 전체 조회")
    void list() {
        // given
        orderTableFindAll();
        // when
        final List<OrderTableResponse> list = tableService.list();
        // then
        assertThat(list).hasSize(1);
    }

    @Test
    @DisplayName("테이블 빈 값 상태 변경")
    void changeEmpty() {
        // given
        doNothing().when(orderTableValidator).validateEnabledClear(any());
        given(orderTableRepository.findById(any()))
                .willReturn(Optional.of(일번_테이블));
        // when
        final OrderTableResponse orderTableResponse = tableService.changeEmpty(일번_테이블.getId(), OrderTable.of(0, false));
        // then
        assertThat(orderTableResponse.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("테이블 사람수 변경")
    void changeNumberOfGuests() {
        // given
        given(orderTableRepository.findById(any()))
                .willReturn(Optional.of(OrderTable.of(0, false)));
        // when
        final OrderTableResponse orderTable = tableService.changeNumberOfGuests(일번_테이블.getId(), OrderTable.of(5, false));
        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(5);
    }

    private void orderTableFindAll() {
        given(orderTableRepository.findAll())
                .willReturn(Collections.singletonList(일번_테이블));
    }
}
