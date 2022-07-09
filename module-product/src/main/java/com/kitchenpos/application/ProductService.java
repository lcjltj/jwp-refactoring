package com.kitchenpos.application;

import com.kitchenpos.domain.Product;
import com.kitchenpos.domain.ProductRepository;
import com.kitchenpos.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final Product product) {
        final Product saveProduct = productRepository.save(product);
        return ProductResponse.of(saveProduct);
    }

    public List<ProductResponse> list() {
        final List<Product> products = productRepository.findAll();
        return ProductResponse.ofList(products);
    }
}
