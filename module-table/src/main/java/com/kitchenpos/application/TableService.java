package com.kitchenpos.application;

import com.kitchenpos.domain.repository.OrderTableRepository;
import com.kitchenpos.validator.OrderTableValidator;
import com.kitchenpos.domain.OrderTable;
import com.kitchenpos.dto.OrderTableResponse;
import com.kitchenpos.exception.TableException;
import com.kitchenpos.exception.TableExceptionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public TableService(final OrderTableRepository orderTableRepository, final OrderTableValidator orderTableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public OrderTableResponse create(final OrderTable orderTable) {
        final OrderTable save = orderTableRepository.save(orderTable);
        return OrderTableResponse.of(save);
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.ofList(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        orderTableValidator.validateEnabledClear(orderTableId);
        final OrderTable savedOrderTable = findById(orderTableId);
        savedOrderTable.updateEmptyTable(orderTable);

        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = findById(orderTableId);
        savedOrderTable.changeNumberOfGuest(orderTable);

        return OrderTableResponse.of(savedOrderTable);
    }

    private OrderTable findById(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new TableException(TableExceptionType.TABLE_NOT_FOUND));
    }
}
