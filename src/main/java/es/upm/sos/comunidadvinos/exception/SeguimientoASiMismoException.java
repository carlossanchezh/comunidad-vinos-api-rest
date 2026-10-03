package es.upm.sos.comunidadvinos.exception;

public class SeguimientoASiMismoException extends RuntimeException {

    public SeguimientoASiMismoException(Long usuarioId) {
        super("El usuario con id ( " + usuarioId + " ) no puede seguir al usuario con id ( " + usuarioId
                + " ). Un usuario no puede seguirse a si mismo ");
    }

}
