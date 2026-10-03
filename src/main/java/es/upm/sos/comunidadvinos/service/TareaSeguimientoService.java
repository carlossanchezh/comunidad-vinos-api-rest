package es.upm.sos.comunidadvinos.service;

import es.upm.sos.comunidadvinos.model.EstadoTarea;
import es.upm.sos.comunidadvinos.model.TareaSeguimiento;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TareaSeguimientoService {

    private final ConcurrentHashMap<Long, TareaSeguimiento> tareas = new ConcurrentHashMap<>();
    private final AtomicLong taskIdGenerator = new AtomicLong(1);

    public TareaSeguimiento crearTarea(Long seguidorId, String seguidorNombre,
            Long seguidoId, String seguidoNombre) {
        TareaSeguimiento tarea = new TareaSeguimiento();
        tarea.setTaskId(taskIdGenerator.getAndIncrement());
        tarea.setSeguidorId(seguidorId);
        tarea.setSeguidorNombre(seguidorNombre);
        tarea.setSeguidoId(seguidoId);
        tarea.setSeguidoNombre(seguidoNombre);
        tarea.setEstado(EstadoTarea.PENDIENTE);
        tarea.setMensaje("Solicitud de seguimiento creada. Esperando respuesta.");
        tarea.setFechaSolicitud(LocalDateTime.now());

        tareas.put(tarea.getTaskId(), tarea);
        return tarea;
    }

    public Optional<TareaSeguimiento> buscarPorId(Long taskId) {
        return Optional.ofNullable(tareas.get(taskId));
    }

    public Page<TareaSeguimiento> findTareasPendientesBySeguidoId(Long seguidoId, int page, int size) {

        List<TareaSeguimiento> tareasPendientes = tareas.values().stream()
                .filter(t -> t.getSeguidoId().equals(seguidoId))
                .filter(t -> t.getEstado() == EstadoTarea.PENDIENTE)
                .collect(Collectors.toList());

        Pageable pageable = PageRequest.of(page, size);

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), tareasPendientes.size());

        List<TareaSeguimiento> tareasPaginadas = tareasPendientes.subList(start, end);

        return new PageImpl<>(tareasPaginadas, pageable, tareasPendientes.size());
    }

    public Optional<TareaSeguimiento> buscarPorSeguidorYSeguido(Long seguidorId, Long seguidoId) {
        return tareas.values().stream()
                .filter(t -> t.getSeguidorId().equals(seguidorId) && t.getSeguidoId().equals(seguidoId))
                .findFirst();
    }

    public void completarTarea(Long taskId, String resultadoUri) {
        TareaSeguimiento tarea = tareas.get(taskId);
        if (tarea != null) {
            tarea.setEstado(EstadoTarea.ACEPTADA);
            tarea.setMensaje("Solicitud aceptada");
            tarea.setResultadoUri(resultadoUri);
            tarea.setFechaRespuesta(LocalDateTime.now());
        }
    }

    public void fallarTarea(Long taskId, String mensaje) {
        TareaSeguimiento tarea = tareas.get(taskId);
        if (tarea != null) {
            tarea.setEstado(EstadoTarea.RECHAZADA);
            tarea.setMensaje(mensaje);
            tarea.setFechaRespuesta(LocalDateTime.now());
        }
    }
}
