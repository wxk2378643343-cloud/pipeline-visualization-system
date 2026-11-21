package tyut.selab.a2.pipes.exception;

/**
 * 转换异常
 */
public class CalulateException extends RuntimeException{

    public CalulateException() {
    }

    public CalulateException(String message) {
        super(message);
    }

    public CalulateException(String message, Throwable cause) {
        super(message, cause);
    }

    public CalulateException(Throwable cause) {
        super(cause);
    }

    protected CalulateException(String message, Throwable cause,
                                boolean enableSuppression,
                                boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
