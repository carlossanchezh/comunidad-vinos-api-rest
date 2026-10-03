package es.upm.sos.comunidadvinos.exception;

public class SeguimientoDejarSeguirNoSigueException extends RuntimeException {

    public SeguimientoDejarSeguirNoSigueException(Long seguidoId, Long seguidorId) {
        super("El usuario con id ( " + seguidorId + " ) esta solicitando dejar de seguir al usuario con id ( "
                + seguidoId
                + " ). Pero el usuario con id ( " + seguidorId + " ) no sigue al usuario con id  ( " + seguidoId
                + " )");
    }

}
