package com.kitchenpos.ui;

import com.kitchenpos.application.ProductService;
import com.kitchenpos.dto.CreateProductRequest;
import com.kitchenpos.dto.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {
    private final ProductService productService;

    public ProductRestController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody final CreateProductRequest createProductRequest) {
        final ProductResponse created = productService.create(createProductRequest.toEntity());
        final URI uri = URI.create("/api/products/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> list() {
        return ResponseEntity.ok().body(productService.list());
    }
}
