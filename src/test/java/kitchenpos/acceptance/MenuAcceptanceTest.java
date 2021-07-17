package kitchenpos.acceptance;

import static kitchenpos.acceptance.MenuAcceptanceTestMethod.*;
import static kitchenpos.acceptance.MenuGroupAcceptanceTestMethod.*;
import static kitchenpos.acceptance.ProductAcceptanceTestMethod.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuAcceptanceTest extends AcceptanceTest {
	@DisplayName("메뉴 등록 및 조회 시나리오")
	@Test
	void createMenuAndFindMenuScenario() {
		// Backgroud
		// Given
		ExtractableResponse<Response> menuGroupResponse = createMenuGroup(new MenuGroup("인기 메뉴"));
		MenuGroup menuGroup = menuGroupResponse.as(MenuGroup.class);
		// And
		ExtractableResponse<Response> productResponse = createProduct(new Product("매운 라면", new BigDecimal(8000)));
		Product product = productResponse.as(Product.class);

		// Scenario
		// When
		ExtractableResponse<Response> menuCreatedResponse = createMenu(new Menu("라면 메뉴", new BigDecimal(8000), menuGroup.getId(), Arrays.asList(new MenuProduct(product.getId(), 2L))));
		Menu createdMenu = menuCreatedResponse.as(Menu.class);
		// Then
		assertThat(menuCreatedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		// When
		ExtractableResponse<Response> menuResponse = findAllMenu();
		// Then
		String menuName = menuResponse.jsonPath().getList(".", Menu.class).stream()
			.filter(menu -> menu.getId() == createdMenu.getId())
			.map(Menu::getName)
			.findFirst()
			.get();
		assertThat(menuName).isEqualTo("라면 메뉴");
	}

	@DisplayName("메뉴 오류 시나리오")
	@Test
	void menuErroScenario() {
		// Backgroud
		// Given
		ExtractableResponse<Response> menuGroupResponse = createMenuGroup(new MenuGroup("인기 메뉴"));
		MenuGroup menuGroup = menuGroupResponse.as(MenuGroup.class);
		// And
		ExtractableResponse<Response> productResponse = createProduct(new Product("매운 라면", new BigDecimal(8000)));
		Product product = productResponse.as(Product.class);

		// Scenario
		// When
		ExtractableResponse<Response> minusPriceResponse = createMenu(new Menu("라면 메뉴", new BigDecimal(-8000), menuGroup.getId(), Arrays.asList(new MenuProduct(product.getId(), 2L))));
		// Then
		assertThat(minusPriceResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// When
		ExtractableResponse<Response> notExistsProductResponse = createMenu(new Menu("라면 메뉴", new BigDecimal(8000), menuGroup.getId(), Arrays.asList(new MenuProduct(0L, 2L))));
		// Then
		assertThat(notExistsProductResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

		// When
		ExtractableResponse<Response> menuPriceIsBiggerThanProductSumResponse = createMenu(new Menu("라면 메뉴", new BigDecimal(1000000), menuGroup.getId(), Arrays.asList(new MenuProduct(product.getId(), 2L))));
		// Then
		assertThat(menuPriceIsBiggerThanProductSumResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
}