package tyut.selab.a2.pipes.exception;

/**
 * 文件传入类型异常
 */
public class ContextException extends RuntimeException{

    public ContextException() {
    }

    public ContextException(String message) {
        super(message);
    }

    public ContextException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContextException(Throwable cause) {
        super(cause);
    }

    protected ContextException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
