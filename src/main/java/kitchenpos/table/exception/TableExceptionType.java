package kitchenpos.table.exception;

public enum TableExceptionType {
    TABLE_NOT_FOUND("테이블이 존재하지 않습니다."),
    EXIST_TABLE_GROUP("그룹 테이블이 존재합니다."),
    IMPOSSIBLE_ORDER_STATUS("현재 주문 상태(요리 중, 식사중)에서는 불가능 합니다."),
    TABLE_EMPTY("미 사용 테이블 입니다."),
    NUMBER_OF_GUESTS_ERROR("손님의 수는 0이상 이여야 합니다.");
    private final String message;

    TableExceptionType(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}