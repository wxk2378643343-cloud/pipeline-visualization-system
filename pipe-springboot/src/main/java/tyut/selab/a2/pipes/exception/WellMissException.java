package tyut.selab.a2.pipes.exception;

public class WellMissException extends RuntimeException{
    public WellMissException() {
    }

    public WellMissException(String message) {
        super(message);
    }

    public WellMissException(String message, Throwable cause) {
        super(message, cause);
    }

    public WellMissException(Throwable cause) {
        super(cause);
    }

    protected WellMissException(String message, Throwable cause,
                                boolean enableSuppression,
                                boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
