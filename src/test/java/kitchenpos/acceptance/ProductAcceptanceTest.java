package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductAcceptanceTest extends AcceptanceTest {

    private static final String API_URL = "/api/products";

    @Test
    @DisplayName("상품 생성 및 조회 시나리오")
    void product() {
        String 상품명 = "치킨";
        BigDecimal 가격 = BigDecimal.valueOf(20_000);

        ExtractableResponse<Response> 상품_생성_요청_결과 = 상품_생성_요청(상품명, 가격);

        상품_생성됨(상품_생성_요청_결과);

        ExtractableResponse<Response> 상품_조회_요청_결과 = 상품_목록_조회_요청();

        상품_목록_조회됨(상품_조회_요청_결과);
    }

    private static ExtractableResponse<Response> 상품_생성_요청(String name, BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(product)
                .when().post(API_URL)
                .then().log().all()
                .extract();
    }

    private void 상품_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 상품_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(API_URL)
                .then().log().all()
                .extract();
    }

    private void 상품_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static Product 상품_등록_되어_있음(String name, BigDecimal price) {
        ExtractableResponse<Response> response = 상품_생성_요청(name, price);

        return response.as(Product.class);
    }
}
