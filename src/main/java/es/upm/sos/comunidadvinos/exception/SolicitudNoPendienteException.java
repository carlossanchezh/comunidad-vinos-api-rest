package es.upm.sos.comunidadvinos.exception;

public class SolicitudNoPendienteException extends RuntimeException {

    public SolicitudNoPendienteException(Long seguidorId, Long seguidoId) {
        super("La solicitud de seguimiento entre el usuario " + seguidorId + " y el usuario " + seguidoId +
                " no está en estado PENDIENTE. No se puede aceptar o rechazar.");
    }
}
