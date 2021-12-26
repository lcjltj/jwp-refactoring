package kitchenpos.ordertable.domain;

import kitchenpos.common.exception.EmptyOrderTableStatusException;
import kitchenpos.common.exception.NegativeNumberOfGuestsException;
import kitchenpos.common.exception.NotEmptyOrderTableStatusException;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.order.domain.Order;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders = new ArrayList<>();

    private int numberOfGuests;

    private boolean empty;


    public OrderTable() {
    }

    public OrderTable(int numberOfGuests) {
        this(null, numberOfGuests);
    }

    public OrderTable(TableGroup tableGroup, int numberOfGuests) {
        this(null, tableGroup, numberOfGuests);
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        checkEmpty(numberOfGuests);
    }

    private void checkEmpty(int numberOfGuests) {
        if (numberOfGuests == 0) {
            empty = true;
            return;
        }

        empty = false;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void validateAddableOrderTable() {
        if (!isEmpty() || Objects.nonNull(tableGroup)) {
            throw new NotEmptyOrderTableStatusException();
        }
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validateChangeableNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validateChangeableNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new NegativeNumberOfGuestsException();
        }

        if (isEmpty()) {
            throw new EmptyOrderTableStatusException();
        }
    }

    public void changeEmpty() {
        validateChangeableOrderTable();
        validateNotProcessing();
        empty = true;
        orders = Collections.emptyList();
    }

    public void validateNotProcessing() {
        for (Order order: orders) {
            order.validateNotProcessing();
        }
    }

    private void validateChangeableOrderTable() {
        if (Objects.nonNull(tableGroup)) {
            throw new NotEmptyOrderTableStatusException();
        }
    }

    public void addOrder(Order order) {
        this.orders.add(order);
        order.decideOrderTable(this);
    }

    public void ungroupTableGroup() {
        for (Order order: orders) {
            order.validateCompleted();
        }

        this.tableGroup = null;
    }
}
