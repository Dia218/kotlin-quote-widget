package exception;

public class QuotesFileAccessException extends RuntimeException {
    private static final String ERROR_MESSAGE = "데이터 접근 중 에러가 발생했습니다: ";
    
    public QuotesFileAccessException(String name) {
        super(String.format(ERROR_MESSAGE + name));
    }
}
