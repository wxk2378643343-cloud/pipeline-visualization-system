package tyut.selab.a2.pipes.exception;

public class ReflectUtilException extends RuntimeException {
    public ReflectUtilException() {
    }

    public ReflectUtilException(String message) {
        super(message);
    }

    public ReflectUtilException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectUtilException(Throwable cause) {
        super(cause);
    }

    public ReflectUtilException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
