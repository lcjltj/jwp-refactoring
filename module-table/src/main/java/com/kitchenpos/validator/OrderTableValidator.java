package com.kitchenpos.validator;

import com.kitchenpos.domain.OrderTable;
import com.kitchenpos.domain.repository.OrderRepository;
import com.kitchenpos.exception.TableException;
import com.kitchenpos.exception.TableExceptionType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderTableValidator {
    private final OrderRepository orderRepository;

    public OrderTableValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateTableSeparate(List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            validateEnabledClear(orderTable.getId());
        }
    }

    public void validateEnabledClear(final Long orderTableId) {
        orderRepository.findByOrderTableId(orderTableId).ifPresent(it -> {
            if (!it.getOrderStatus().enabledTableClear()) {
                throw new TableException(TableExceptionType.IMPOSSIBLE_ORDER_STATUS);
            }
        });
    }
}
