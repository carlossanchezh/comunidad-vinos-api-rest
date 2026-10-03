package es.upm.sos.comunidadvinos.exception;

public class UsuarioYaExisteException extends RuntimeException {

    public UsuarioYaExisteException(String correo) {
        super("El correo " + correo + " ya esta siendo utilizado por otro usuario");
    }

}
