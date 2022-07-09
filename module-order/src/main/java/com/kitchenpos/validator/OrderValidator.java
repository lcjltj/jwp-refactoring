package com.kitchenpos.validator;

import com.kitchenpos.dto.OrderRequest;
import com.kitchenpos.domain.OrderTable;
import com.kitchenpos.domain.repository.OrderTableRepository;
import com.kitchenpos.exception.TableException;
import com.kitchenpos.exception.TableExceptionType;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(final OrderRequest orderRequest) {
        final OrderTable orderTable = findByOrderTableId(orderRequest);
        orderTable.validateUsed();
    }

    private OrderTable findByOrderTableId(final OrderRequest orderRequest) {
        return orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new TableException(TableExceptionType.TABLE_NOT_FOUND));
    }
}
