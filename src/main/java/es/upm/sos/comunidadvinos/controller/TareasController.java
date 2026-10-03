package es.upm.sos.comunidadvinos.controller;

import es.upm.sos.comunidadvinos.exception.TareaNotFoundException;
import es.upm.sos.comunidadvinos.model.EstadoTarea;
import es.upm.sos.comunidadvinos.model.TareaSeguimiento;
import es.upm.sos.comunidadvinos.service.TareaSeguimientoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;

@RestController
@RequestMapping("/tareas")
@AllArgsConstructor
public class TareasController {

    private final TareaSeguimientoService tareaService;

    @GetMapping("/{taskId}")
    public ResponseEntity<TareaSeguimiento> getTarea(@PathVariable Long taskId) {
        TareaSeguimiento tarea = tareaService.buscarPorId(taskId).orElseThrow(() -> new TareaNotFoundException(taskId));

        if (tarea.getEstado() == EstadoTarea.ACEPTADA) {
            return ResponseEntity.status(303)
                    .location(URI.create(tarea.getResultadoUri()))
                    .build();
        }

        tarea.removeLinks();

        tarea.add(linkTo(methodOn(TareasController.class).getTarea(taskId)).withSelfRel());

        return ResponseEntity.ok(tarea);
    }
}
