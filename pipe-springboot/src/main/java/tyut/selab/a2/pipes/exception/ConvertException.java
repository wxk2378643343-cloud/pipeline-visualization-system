package tyut.selab.a2.pipes.exception;

/**
 * 转换类型异常
 */
public class ConvertException extends RuntimeException{

    public ConvertException() {
    }

    public ConvertException(String message) {
        super(message);
    }

    public ConvertException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConvertException(Throwable cause) {
        super(cause);
    }

    protected ConvertException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
