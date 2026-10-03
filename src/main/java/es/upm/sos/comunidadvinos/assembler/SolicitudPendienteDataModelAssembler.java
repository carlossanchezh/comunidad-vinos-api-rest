package es.upm.sos.comunidadvinos.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import es.upm.sos.comunidadvinos.controller.UsuarioController;
import es.upm.sos.comunidadvinos.controller.TareasController;
import es.upm.sos.comunidadvinos.model.SolicitudPendienteData;
import es.upm.sos.comunidadvinos.model.TareaSeguimiento;

@Component
public class SolicitudPendienteDataModelAssembler
                extends RepresentationModelAssemblerSupport<TareaSeguimiento, SolicitudPendienteData> {

        public SolicitudPendienteDataModelAssembler() {
                super(UsuarioController.class, SolicitudPendienteData.class);
        }

        @Override
        public SolicitudPendienteData toModel(TareaSeguimiento tarea) {

                SolicitudPendienteData solicitud = new SolicitudPendienteData();

                solicitud.setTaskId(tarea.getTaskId());
                solicitud.setSeguidorId(tarea.getSeguidorId());
                solicitud.setSeguidorNombre(tarea.getSeguidorNombre());
                solicitud.setFechaSolicitud(tarea.getFechaSolicitud());

                // Link a la tarea individual
                solicitud.add(linkTo(methodOn(TareasController.class)
                                .getTarea(tarea.getTaskId()))
                                .withRel("tarea"));

                // Link a aceptar (usando seguidoId de la tarea)
                solicitud.add(linkTo(methodOn(UsuarioController.class)
                                .actualizarSolicitud(tarea.getSeguidoId(), tarea.getSeguidorId(), null))
                                .withRel("aceptar o rechazar"));

                // Link al seguidor
                solicitud.add(linkTo(methodOn(UsuarioController.class)
                                .getUsuario(tarea.getSeguidorId()))
                                .withRel("seguidor"));

                return solicitud;
        }
}
