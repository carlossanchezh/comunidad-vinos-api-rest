package es.upm.sos.comunidadvinos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class VinoExceptionAdvice {

    @ExceptionHandler(VinoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage vinoNotFoundHandler(VinoNotFoundException ex) {
        return new ErrorMessage(ex.getMessage());
    }

}
