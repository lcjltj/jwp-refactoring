package kitchenpos.menu.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import com.kitchenpos.dto.CreateMenuGroupRequest;
import com.kitchenpos.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuGroupAcceptanceTest extends AcceptanceTest {
    private static final String MENU_GROUP_URL = "/api/menu-groups";
    public static final CreateMenuGroupRequest 햄버거_메뉴 = new CreateMenuGroupRequest("햄버거메뉴");

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    /**
     * when 메뉴 그룹을 추가 한다.
     * then 메뉴 그룹이 추가됨.
     * <p>
     * when 메뉴 그룹을 조회 한다.
     * then 추가된 메뉴 그룹이 조회됨.
     */
    @Test
    @DisplayName("메뉴-그룹 관리")
    void menuGroup() {
        // when
        final ExtractableResponse<Response> 메뉴_그룹_추가_요청 = 메뉴_그룹_추가_요청(햄버거_메뉴);
        // then
        final MenuGroupResponse 메뉴_그룹_추가_됨 = 메뉴_그룹_추가_됨(메뉴_그룹_추가_요청);

        // when
        final ExtractableResponse<Response> 전체_메뉴_그룹_조회_요청 = 전체_메뉴_그룹_조회_요청();
        // then
        final List<MenuGroupResponse> menuGroups = 전체_메뉴_그룹_조회_됨(전체_메뉴_그룹_조회_요청);
        assertThat(menuGroups).contains(메뉴_그룹_추가_됨);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_추가_요청(final CreateMenuGroupRequest menuGroup) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuGroup)
                .when().post(MENU_GROUP_URL)
                .then().log().all()
                .extract();
    }

    public static MenuGroupResponse 메뉴_그룹_추가_됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.as(MenuGroupResponse.class);
    }

    public static MenuGroupResponse 메뉴_그룹_추가_되어_있음(final CreateMenuGroupRequest menuGroup) {
        return 메뉴_그룹_추가_됨(메뉴_그룹_추가_요청(menuGroup));
    }

    public static ExtractableResponse<Response> 전체_메뉴_그룹_조회_요청() {
        return RestAssured.given().log().all()
                .when().get(MENU_GROUP_URL)
                .then().log().all()
                .extract();
    }
    public List<MenuGroupResponse> 전체_메뉴_그룹_조회_됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response.jsonPath().getList(".", MenuGroupResponse.class);
    }
}
