package es.upm.sos.comunidadvinos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public class UvaExceptionAdvice {

    @ExceptionHandler(UvaNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage uvaNotFoundHandler(UvaNotFoundException ex) {
        return new ErrorMessage(ex.getMessage());
    }

}
