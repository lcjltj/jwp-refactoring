package kitchenpos.order.domain.value;

import java.util.Collections;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.order.domain.entity.OrderTable;
import kitchenpos.order.domain.entity.TableGroup;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    public OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void toTableGroup(TableGroup tableGroup) {
        orderTables.forEach(orderTable -> orderTable.toTableGroup(tableGroup));
    }

    public List<OrderTable> getValue() {
        return Collections.unmodifiableList(orderTables);
    }
}