package tyut.selab.a2.pipes.exception;

public class IllegalReturnClassTypeException extends RuntimeException{
    public IllegalReturnClassTypeException() {
    }

    public IllegalReturnClassTypeException(String message) {
        super(message);
    }

    public IllegalReturnClassTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalReturnClassTypeException(Throwable cause) {
        super(cause);
    }

    public IllegalReturnClassTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
