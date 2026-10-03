package es.upm.sos.comunidadvinos.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import es.upm.sos.comunidadvinos.model.VinoPuntuacionData;
import es.upm.sos.comunidadvinos.controller.VinoController;
import es.upm.sos.comunidadvinos.model.UsuarioVino;

@Component
public class VinoPuntuacionDataModelAssembler
        extends RepresentationModelAssemblerSupport<UsuarioVino, VinoPuntuacionData> {

    public VinoPuntuacionDataModelAssembler() {
        super(VinoController.class, VinoPuntuacionData.class);
    }

    @Override
    public VinoPuntuacionData toModel(UsuarioVino usuarioVino) {

        VinoPuntuacionData vinoPuntuacion = new VinoPuntuacionData();
        vinoPuntuacion.setId(usuarioVino.getVino().getId());
        vinoPuntuacion.setNombre(usuarioVino.getVino().getNombre());
        vinoPuntuacion.setBodega(usuarioVino.getVino().getBodega());
        vinoPuntuacion.setAnada(usuarioVino.getVino().getAnada());
        vinoPuntuacion.setOrigen(usuarioVino.getVino().getOrigen());
        vinoPuntuacion.setTipo(usuarioVino.getVino().getTipo());
        vinoPuntuacion.setPuntuacion(usuarioVino.getPuntuacion());
        vinoPuntuacion.setFechaAnadido(usuarioVino.getFechaAnadido());

        // Link al vino
        vinoPuntuacion.add(linkTo(methodOn(VinoController.class).getVino(usuarioVino.getVino().getId()))
                .withSelfRel());

        return vinoPuntuacion;
    }

}
