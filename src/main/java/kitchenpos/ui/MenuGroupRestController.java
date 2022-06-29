package kitchenpos.ui;

import kitchenpos.application.MenuGroupService;
import kitchenpos.ui.dto.MenuGroupCreateRequest;
import kitchenpos.ui.dto.MenuGroupCreateResponse;
import kitchenpos.ui.dto.MenuGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MenuGroupRestController {
    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping("/api/menu-groups")
    public ResponseEntity<MenuGroupCreateResponse> create(@RequestBody final MenuGroupCreateRequest request) {
        final MenuGroupCreateResponse created = new MenuGroupCreateResponse(menuGroupService.create(request.toEntity()));
        final URI uri = URI.create("/api/menu-groups/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupResponse>> list() {
        return ResponseEntity.ok()
                .body(menuGroupService.list()
                        .stream()
                        .map(MenuGroupResponse::new)
                        .collect(Collectors.toList())
                )
                ;
    }
}
