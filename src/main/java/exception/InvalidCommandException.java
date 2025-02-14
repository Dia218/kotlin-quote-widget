package exception;

public class InvalidCommandException extends Exception {
    private static final String ERROR_MESSAGE = "잘못된 명령어 입니다: ";
    
    public InvalidCommandException(String errorCommand) {
        super(String.format(ERROR_MESSAGE + errorCommand));
    }
}
