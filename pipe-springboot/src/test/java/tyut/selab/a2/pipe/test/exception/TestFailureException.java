package tyut.selab.a2.pipe.test.exception;

public class TestFailureException extends RuntimeException {
    public TestFailureException() {
    }

    public TestFailureException(String message) {
        super(message);
    }

    public TestFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public TestFailureException(Throwable cause) {
        super(cause);
    }

    public TestFailureException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
