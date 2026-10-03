package es.upm.sos.comunidadvinos.exception;

public class UsuarioVinoNotFoundException extends RuntimeException {

    public UsuarioVinoNotFoundException(Long usuarioId, Long vinoId) {
        super("El vino de id " + vinoId + " no pertenece a la lista del usuario de id " + usuarioId);
    }

}
