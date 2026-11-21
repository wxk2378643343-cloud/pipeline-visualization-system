package tyut.selab.a2.pipes.exception;

/**
 * 文件传入类型异常
 */
public class FileIOException extends RuntimeException{

    public FileIOException() {
    }

    public FileIOException(String message) {
        super(message);
    }

    public FileIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileIOException(Throwable cause) {
        super(cause);
    }

    protected FileIOException(String message, Throwable cause,
                              boolean enableSuppression,
                              boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
