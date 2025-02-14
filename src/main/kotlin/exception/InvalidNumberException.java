package exception;

public class InvalidNumberException extends NumberFormatException {
    private static final String ERROR_MESSAGE = "숫자만 입력해주세요. 입력된 값: ";
  
    public InvalidNumberException(String errorNum) {
        super(ERROR_MESSAGE + errorNum);
    }
}
