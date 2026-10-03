package es.upm.sos.comunidadvinos.exception;

public class UsuarioVinoYaExisteException extends RuntimeException {

    public UsuarioVinoYaExisteException(String nombreUsuario, String nombreVino) {
        super("El vino " + nombreVino + " ya esta en la lista del usuario " + nombreUsuario);
    }

}
