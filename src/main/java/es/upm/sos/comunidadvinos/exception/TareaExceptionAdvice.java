package es.upm.sos.comunidadvinos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TareaExceptionAdvice {

    @ExceptionHandler(TareaNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage tareaNotFoundException(TareaNotFoundException ex) {
        return new ErrorMessage(ex.getMessage());
    }
}
