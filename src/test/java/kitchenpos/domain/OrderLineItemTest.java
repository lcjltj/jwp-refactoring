package kitchenpos.domain;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemTest {

    @Test
    @DisplayName("주문 항목에 주문 등록 테스트")
    void registerOrderTest() {

        //given
        OrderTable orderTable = new OrderTable(10, false);
        OrderLineItem orderLineItem = new OrderLineItem(1L, 10);

        //when
        Order order = new Order(orderTable.getId(), orderLineItem);

        //then
        assertThat(orderLineItem.getOrder()).isNotNull();
    }
}