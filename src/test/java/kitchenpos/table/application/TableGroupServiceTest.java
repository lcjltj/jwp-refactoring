package kitchenpos.table.application;

import com.kitchenpos.application.TableGroupService;
import kitchenpos.menu.domain.MenuTest;
import com.kitchenpos.domain.Order;
import com.kitchenpos.domain.OrderLineItem;
import com.kitchenpos.domain.OrderStatus;
import com.kitchenpos.domain.OrderTable;
import com.kitchenpos.domain.TableGroup;
import com.kitchenpos.domain.repository.OrderTableRepository;
import com.kitchenpos.domain.repository.TableGroupRepository;
import com.kitchenpos.dto.TableGroupRequest;
import com.kitchenpos.dto.TableGroupResponse;
import com.kitchenpos.validator.OrderTableValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @Mock
    private OrderTableValidator orderTableValidator;
    @InjectMocks
    TableGroupService tableGroupService;

    @Test
    @DisplayName("테이블 병합")
    void create() {
        // given
        OrderTable 일번_테이블 = OrderTable.of(0, true);
        OrderTable 이번_테이블 = OrderTable.of(0, true);
        given(orderTableRepository.findAllById(any()))
                .willReturn(Arrays.asList(일번_테이블, 이번_테이블));
        given(tableGroupRepository.save(any()))
                .willReturn(TableGroup.of(Arrays.asList(일번_테이블, 이번_테이블), 2));

        // when
        final TableGroupResponse tableGroup = tableGroupService.create(new TableGroupRequest(Arrays.asList(일번_테이블, 이번_테이블)));
        // then
        assertThat(tableGroup)
                .isInstanceOf(TableGroupResponse.class);
    }

    @Test
    @DisplayName("테이블 분리")
    void ungroup() {
        // given
        final OrderTable 일번_테이블 = OrderTable.of(0, true);
        final OrderTable 이번_테이블 = OrderTable.of(0, true);
        final Order order = Order.of(일번_테이블.getId(), Arrays.asList(OrderLineItem.of(MenuTest.햄버거메뉴, 1L)));
        order.updateOrderStatus(OrderStatus.COMPLETION);

        given(orderTableRepository.findAllById(any()))
                .willReturn(Arrays.asList(일번_테이블, 이번_테이블));
        given(tableGroupRepository.findById(any()))
                .willReturn(Optional.of(TableGroup.of(Arrays.asList(일번_테이블, 이번_테이블), 2)));
        doNothing().when(orderTableValidator).validateTableSeparate(any());
        // when
        tableGroupService.ungroup(1L);
        // then
        assertThat(일번_테이블.getTableGroupId()).isNull();
    }
}
