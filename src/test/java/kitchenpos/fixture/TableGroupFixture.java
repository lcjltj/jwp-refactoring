package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupResponseDto;

public class TableGroupFixture {

    public static TableGroup 단체_지정_데이터_생성(List<OrderTable> orderTables) {
//        return new TableGroup(null, null, orderTables);
        return null;
    }

    public static TableGroup 단체_데이터_생성(Long id) {
        return new TableGroup(id);
    }

    public static TableGroupResponseDto 단체_응답_데이터_생성(Long id) {
        return new TableGroupResponseDto(id, LocalDateTime.now());
    }

}
