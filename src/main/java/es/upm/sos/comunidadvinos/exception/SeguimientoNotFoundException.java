package es.upm.sos.comunidadvinos.exception;

public class SeguimientoNotFoundException extends RuntimeException {

    public SeguimientoNotFoundException(Long seguidorId, Long seguidoId) {
        super("El usuario con id ( " + seguidorId + " ) no sigue al usuario con id ( " + seguidoId + " )");
    }

}
