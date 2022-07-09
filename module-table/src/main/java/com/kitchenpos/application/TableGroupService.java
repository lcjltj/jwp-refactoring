package com.kitchenpos.application;

import com.kitchenpos.domain.TableGroup;
import com.kitchenpos.domain.repository.OrderTableRepository;
import com.kitchenpos.domain.repository.TableGroupRepository;
import com.kitchenpos.exception.TableException;
import com.kitchenpos.exception.TableExceptionType;
import com.kitchenpos.validator.OrderTableValidator;
import com.kitchenpos.domain.OrderTable;
import com.kitchenpos.dto.TableGroupRequest;
import com.kitchenpos.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableValidator orderTableValidator;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository,
                             final OrderTableValidator orderTableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTable> savedOrderTables = getOrderTables(tableGroupRequest.fetchOrderTableIds());
        final TableGroup tableGroup = TableGroup.of(savedOrderTables, tableGroupRequest.size());

        final TableGroup save = tableGroupRepository.save(tableGroup);
        save.updateGroupTableId();

        return TableGroupResponse.of(save);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = findByTableGroupId(tableGroupId);
        final List<OrderTable> savedOrderTables = getOrderTables(tableGroup.getOrderTableIds());
        orderTableValidator.validateTableSeparate(savedOrderTables);
        tableGroup.ungroupTable();
    }

    private TableGroup findByTableGroupId(final Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new TableException(TableExceptionType.TABLE__GROUP_NOT_FOUND));
    }

    private List<OrderTable> getOrderTables(final List<Long> groupIds) {
        return orderTableRepository.findAllById(groupIds);
    }
}
