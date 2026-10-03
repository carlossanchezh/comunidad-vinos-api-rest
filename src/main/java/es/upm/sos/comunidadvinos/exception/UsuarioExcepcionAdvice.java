package es.upm.sos.comunidadvinos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UsuarioExcepcionAdvice {

    @ExceptionHandler(UsuarioNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage usuarioNotFoundHandler(UsuarioNotFoundException ex) {
        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler(UsuarioMenorDeEdadException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage usuarioMenorDeEdadHandler(UsuarioMenorDeEdadException ex) {
        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler(UsuarioYaExisteException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage usuarioYaExisteException(UsuarioYaExisteException ex) {
        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler(UsuarioVinoYaExisteException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage usuarioVinoYaExisteException(UsuarioVinoYaExisteException ex) {
        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler(UsuarioVinoNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage usuarioVinoNotFoundException(UsuarioVinoNotFoundException ex) {
        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler(SeguimientoNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage seguimientoNotFoundException(SeguimientoNotFoundException ex) {
        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler(SeguimientoYaExisteException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage SeguimientoYaExisteException(SeguimientoYaExisteException ex) {
        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler(SeguimientoASiMismoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage SeguimientoASiMismoException(SeguimientoASiMismoException ex) {
        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler(SolicitudNoPendienteException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage solicitudNoPendienteException(SolicitudNoPendienteException ex) {
        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler(SolicitudEstadoNoValidoActualizarException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage SolicitudEstadoNoValidoActualizarException(SolicitudEstadoNoValidoActualizarException ex) {
        return new ErrorMessage(ex.getMessage());
    }

}
