package kitchenpos.common.exception;

public class EmptyOrderTableException extends RuntimeException {
    public static final String EMPTY_ORDER_TABLE_EXCEPTION = "주문 테이블은 비어 있을 수 없습니다";
    public EmptyOrderTableException() {
        super(EMPTY_ORDER_TABLE_EXCEPTION);
    }
}
