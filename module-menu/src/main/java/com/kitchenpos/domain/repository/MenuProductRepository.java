package com.kitchenpos.domain.repository;

import com.kitchenpos.domain.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
    List<MenuProduct> findAllByMenuId(Long menId);
}
