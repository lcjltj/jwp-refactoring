package kitchenpos.order.application;

import kitchenpos.common.exception.EmptyOrderTableException;
import kitchenpos.common.exception.NotFoundEntityException;
import kitchenpos.common.exception.OrderStatusCompletedException;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@DisplayName("주문 서비스 관련 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    private OrderService orderService;

    private Order 주문;
    private OrderRequest 주문_요청;
    private OrderLineItem 주문_항목;
    private OrderLineItem 주문_항목2;
    private OrderTable 주문_테이블_1번;
    private OrderTable 주문_테이블_2번;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository, orderTableRepository);
        주문_항목 = new OrderLineItem();
        주문_항목2 = new OrderLineItem();
        주문 = new Order(new OrderTable(), Lists.newArrayList(주문_항목, 주문_항목2));

        주문_테이블_1번 = new OrderTable(1L, null, 3);
        주문_테이블_2번 = new OrderTable(null, 0);
        주문_요청 = new OrderRequest(주문_테이블_1번.getId(), Lists.newArrayList(주문_항목, 주문_항목2));
    }

    @DisplayName("주문을 등록할 수 있다.")
    @Test
    void createOrderTest() {
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(주문_테이블_1번));
        when(orderRepository.save(any())).thenReturn(주문);

        // when
        OrderResponse createdOrder = 주문_요청_한다(주문_요청);

        // then
        assertAll(
                () -> assertThat(createdOrder.getOrderLineItems().get(0)).isEqualTo(주문_항목),
                () -> assertThat(createdOrder.getOrderLineItems().get(1)).isEqualTo(주문_항목2)
        );
    }

    @DisplayName("주문 테이블이 존재해야 한다. (주문 테이블이 존재해야 한다)")
    @Test
    void createOrderExistOrderTableExceptionTest() {
        assertThatThrownBy(() -> {
            // given
            final OrderRequest emptyOrderTableItem = new OrderRequest(null, Lists.newArrayList(주문_항목, 주문_항목2));

            // when
            OrderResponse createdOrder = 주문_요청_한다(emptyOrderTableItem);

            // then
        }).isInstanceOf(NotFoundEntityException.class);
    }

    @DisplayName("주문 테이블이 존재해야 한다. (주문 테이블이 빈 상태가 아니어야 한다)")
    @Test
    void createOrderNotEmptyOrderTableExceptionTest() {
        assertThatThrownBy(() -> {
            when(orderTableRepository.findById(any())).thenReturn(Optional.of(주문_테이블_2번));

            // given
            final OrderRequest emptyOrderTableItem = new OrderRequest(주문_테이블_2번.getId(), Lists.newArrayList(주문_항목, 주문_항목2));

            // when
            OrderResponse createdOrder = 주문_요청_한다(emptyOrderTableItem);

            // then
        }).isInstanceOf(EmptyOrderTableException.class);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void getOrderTest() {
        when(orderRepository.findAll()).thenReturn(Lists.newArrayList(주문));

        // when
        List<OrderResponse> createdOrders = 주문_목록_조회한다();

        // then
        assertThat(createdOrders.get(0).getId()).isEqualTo(주문.getId());
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatusTest() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(주문));

        // given
        OrderRequest orderRequest = new OrderRequest(주문_테이블_1번.getId(), "MEAL");

        // when
        OrderResponse changedOrders = orderService.changeOrderStatus(2L, orderRequest);

        // then
        assertThat(changedOrders.getId()).isEqualTo(주문.getId());
    }

    @DisplayName("주문 id는 반드시 존재한다.")
    @Test
    void changeOrderIdExceptionTest() {
        assertThatThrownBy(() -> {
            // when
            OrderResponse changedOrders = orderService.changeOrderStatus(null, 주문_요청);

            // then
        }).isInstanceOf(NotFoundEntityException.class);
    }

    @DisplayName("주문 상태는 완료가 아니어야 한다.")
    @Test
    void changeOrderNotCompleteStatusExceptionTest() {
        assertThatThrownBy(() -> {
            // given
            주문.changeOrderStatus(OrderStatus.COMPLETION);

            // when
            OrderResponse changedOrders = orderService.changeOrderStatus(2L, 주문_요청);

            // then
        }).isInstanceOf(OrderStatusCompletedException.class);
    }

    private OrderResponse 주문_요청_한다(OrderRequest orderRequest) {
        return orderService.create(orderRequest);
    }

    private List<OrderResponse> 주문_목록_조회한다() {
        return orderService.list();
    }


}