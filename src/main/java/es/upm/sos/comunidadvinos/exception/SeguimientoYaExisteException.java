package es.upm.sos.comunidadvinos.exception;

public class SeguimientoYaExisteException extends RuntimeException {

    public SeguimientoYaExisteException(Long seguidorId, Long seguidoId) {
        super("El usuario con id ( " + seguidorId + " ) ya sigue usuario con id ( " + seguidoId
                + " ). No se puede volver a seguir a un usuario que ya se sigue");
    }

}
