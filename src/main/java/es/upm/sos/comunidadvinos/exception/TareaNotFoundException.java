package es.upm.sos.comunidadvinos.exception;

public class TareaNotFoundException extends RuntimeException {

    public TareaNotFoundException(Long taskId) {
        super("Tarea con id " + taskId + " no encontrada");
    }
}
