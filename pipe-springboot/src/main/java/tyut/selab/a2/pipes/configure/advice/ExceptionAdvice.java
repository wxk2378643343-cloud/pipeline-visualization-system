package tyut.selab.a2.pipes.configure.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tyut.selab.a2.pipes.exception.*;
import tyut.selab.a2.pipes.entity.Result;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {
    @ExceptionHandler(TestException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result handleNormalApplicationException(TestException exception) {
        log.warn("A unknown runtime exception: {}", exception.getMessage());
        return Result.error(exception.getMessage());
    }

    @ExceptionHandler(CalulateException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result handleNormalApplicationException(CalulateException exception) {
        log.warn("A unknown runtime exception: {}", exception.getMessage());
        return Result.error(Result.REREQUEST, exception.getMessage());
    }

    @ExceptionHandler(WellMissException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result handleWellMissException(WellMissException exception) {
        log.warn("well data missing exception: {}", exception.getMessage());
        return Result.error(exception.getMessage());
    }

    @ExceptionHandler(ConvertException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result handleWellMissException(ConvertException exception) {
        log.warn("A unknown runtime exception: {}", exception.getMessage());
        return Result.error(exception.getMessage());
    }

    @ExceptionHandler(FileTypeException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result handleWellMissException(FileTypeException exception) {
        log.warn("A unknown runtime exception: {}", exception.getMessage());
        return Result.error(exception.getMessage());
    }

    @ExceptionHandler(ContextException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result handleWellMissException(ContextException exception) {
        log.warn("A unknown runtime exception: {}", exception.getMessage());
        return Result.error(exception.getMessage());
    }

    @ExceptionHandler(CellTypeException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result handleWellMissException(CellTypeException exception) {
        log.warn("A unknown runtime exception: {}", exception.getMessage());
        return Result.error(exception.getMessage());
    }

    @ExceptionHandler(IllegalReturnClassTypeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result handleNormalApplicationException(IllegalReturnClassTypeException exception) {
        log.error("Return type is illegal:", exception);
        return Result.error(exception.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result handleNormalApplicationException(RuntimeException exception) {
        log.error("A unknown runtime exception:", exception);
        return Result.error(exception.getMessage());
    }
}
