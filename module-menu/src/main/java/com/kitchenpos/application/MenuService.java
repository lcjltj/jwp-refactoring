package com.kitchenpos.application;

import com.kitchenpos.domain.Menu;
import com.kitchenpos.domain.repository.MenuRepository;
import com.kitchenpos.dto.CreateMenuRequest;
import com.kitchenpos.dto.MenuResponse;
import com.kitchenpos.validator.MenuValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(final MenuRepository menuRepository, final MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final CreateMenuRequest createMenuRequest) {
        menuValidator.validate(createMenuRequest);
        final Menu savedMenu = menuRepository.save(createMenuRequest.toEntity());
        savedMenu.addMenuProducts(createMenuRequest.toMenuProducts());

        return MenuResponse.of(savedMenu);
    }


    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return MenuResponse.ofList(menus);
    }
}
