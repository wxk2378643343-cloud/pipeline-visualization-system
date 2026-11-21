package tyut.selab.a2.pipes.exception;

public class DataIsNullException extends RuntimeException{
    public DataIsNullException() {
    }

    public DataIsNullException(String message) {
        super(message);
    }

    public DataIsNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataIsNullException(Throwable cause) {
        super(cause);
    }

    protected DataIsNullException(String message, Throwable cause,
                                boolean enableSuppression,
                                boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
