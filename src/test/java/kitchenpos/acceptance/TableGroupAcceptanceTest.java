package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class TableGroupAcceptanceTest extends AcceptanceTest {
    private static final String TABLE_GROUP_URL = "/api/table-groups";
    public TableGroup 테이블_그룹;
    public OrderTable 구번_테이블;
    public OrderTable 십번_테이블;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        구번_테이블 = new OrderTable();
        구번_테이블.setEmpty(true);
        구번_테이블.setNumberOfGuests(0);

        십번_테이블 = new OrderTable();
        십번_테이블.setEmpty(true);
        십번_테이블.setNumberOfGuests(0);
    }

    /**
     * when 테이블 그룹 추가 한다.
     * then 테이블 그룹이 추가 됨.
     * when 테이블 그룹을 조회 한다
     * then 추가된 테이블 그룹이 조회 됨.
     */
    @Test
    @DisplayName("제품 관리 테스트")
    void tableGroup() {
        // given
        final OrderTable 구번_테이블_생성 = TableAcceptanceTest.테이블_추가_되어_있음(구번_테이블);
        final OrderTable 십번_테이블_생성 = TableAcceptanceTest.테이블_추가_되어_있음(십번_테이블);
        테이블_그룹 = new TableGroup();
        테이블_그룹.setOrderTables(Arrays.asList(구번_테이블_생성, 십번_테이블_생성));
        // when
        final ExtractableResponse<Response> 테이블_그룹_추가_요청 = 테이블_그룹_추가_요청(테이블_그룹);
        // then
        final TableGroup tableGroup = 테이블_그룹_추가_됨(테이블_그룹_추가_요청);

        // when
        final ExtractableResponse<Response> 테이블_그룹_삭제_요청 = 테이블_그룹_삭제_요청(tableGroup.getId());
        // then
        테이블_그룹_삭제_됨(테이블_그룹_삭제_요청);
    }

    public static ExtractableResponse<Response> 테이블_그룹_추가_요청(final TableGroup tableGroup) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tableGroup)
                .when().post(TABLE_GROUP_URL)
                .then().log().all()
                .extract();
    }

    public static TableGroup 테이블_그룹_추가_됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.as(TableGroup.class);
    }

    public static TableGroup 테이블_그룹_추가_되어_있음(final TableGroup tableGroup) {
        return 테이블_그룹_추가_됨(테이블_그룹_추가_요청(tableGroup));
    }

    public static ExtractableResponse<Response> 테이블_그룹_삭제_요청(final Long tableGroupId) {
        return RestAssured.given().log().all()
                .when().delete(TABLE_GROUP_URL + "/{tableGroupId}", tableGroupId)
                .then().log().all()
                .extract();
    }

    public static void 테이블_그룹_삭제_됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}