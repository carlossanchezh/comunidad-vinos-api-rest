package es.upm.sos.comunidadvinos.exception;

public class UvaNotFoundException extends RuntimeException {

    public UvaNotFoundException(Long id) {
        super("Uva con id (" + id + ") no encontrada");
    }

}
