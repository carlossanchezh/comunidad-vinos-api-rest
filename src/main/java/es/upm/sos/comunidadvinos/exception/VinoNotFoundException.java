package es.upm.sos.comunidadvinos.exception;

public class VinoNotFoundException extends RuntimeException {

    public VinoNotFoundException(Long id) {
        super("Vino con id (" + id + ") no encontrado");
    }

}
