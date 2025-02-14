package exception;

public class QuoteNotFoundException extends RuntimeException {
    private static final String ERROR_MESSAGE = "%d번 명언은 존재하지 않습니다.";
    
    public QuoteNotFoundException(int quoteId) {
        super(String.format(ERROR_MESSAGE, quoteId));
    }
}
