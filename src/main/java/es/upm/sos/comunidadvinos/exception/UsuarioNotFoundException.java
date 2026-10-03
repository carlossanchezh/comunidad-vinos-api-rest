package es.upm.sos.comunidadvinos.exception;

public class UsuarioNotFoundException extends RuntimeException {

    public UsuarioNotFoundException(Long id) {
        super("Usuario con id (" + id + ") no encontrado");
    }

}
