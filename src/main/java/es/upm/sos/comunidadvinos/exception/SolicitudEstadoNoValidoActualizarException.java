package es.upm.sos.comunidadvinos.exception;

public class SolicitudEstadoNoValidoActualizarException extends RuntimeException {

    public SolicitudEstadoNoValidoActualizarException(Long seguidorId, Long seguidoId) {
        super("La solicitud de seguimiento entre el usuario " + seguidorId + " y el usuario " + seguidoId +
                " que esta intentando actualizar requiere del estado ACEPTADA o RECHAZADA para ser actualizada correctamente. Esta introduciendo un nuevo esatdo no valido");
    }
}
