package com.kitchenpos.ui;

import com.kitchenpos.application.TableService;
import com.kitchenpos.domain.OrderTable;
import com.kitchenpos.dto.CreateOrderTableRequest;
import com.kitchenpos.dto.OrderTableResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class TableRestController {
    private final TableService tableService;

    public TableRestController(final TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/api/tables")
    public ResponseEntity<OrderTableResponse> create(@RequestBody final CreateOrderTableRequest createOrderTableRequest) {
        final OrderTableResponse created = tableService.create(createOrderTableRequest.toEntity());
        final URI uri = URI.create("/api/tables/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping("/api/tables")
    public ResponseEntity<List<OrderTableResponse>> list() {
        return ResponseEntity.ok().body(tableService.list());
    }

    @PutMapping("/api/tables/{orderTableId}/empty")
    public ResponseEntity<OrderTableResponse> changeEmpty(@PathVariable final Long orderTableId,
                                                          @RequestBody final OrderTable orderTable) {
        return ResponseEntity.ok().body(tableService.changeEmpty(orderTableId, orderTable));
    }

    @PutMapping("/api/tables/{orderTableId}/number-of-guests")
    public ResponseEntity<OrderTableResponse> changeNumberOfGuests(@PathVariable final Long orderTableId,
                                                                   @RequestBody final OrderTable orderTable) {
        return ResponseEntity.ok().body(tableService.changeNumberOfGuests(orderTableId, orderTable));
    }
}
