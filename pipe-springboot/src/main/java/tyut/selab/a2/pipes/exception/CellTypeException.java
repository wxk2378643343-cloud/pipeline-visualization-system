package tyut.selab.a2.pipes.exception;

/**
 * 单元格类型异常
 */
public class CellTypeException extends RuntimeException{

    public CellTypeException() {
    }

    public CellTypeException(String message) {
        super(message);
    }

    public CellTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CellTypeException(Throwable cause) {
        super(cause);
    }

    protected CellTypeException(String message, Throwable cause,
                                boolean enableSuppression,
                                boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
