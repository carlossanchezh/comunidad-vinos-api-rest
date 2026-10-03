package es.upm.sos.comunidadvinos.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import es.upm.sos.comunidadvinos.controller.UsuarioController;
import es.upm.sos.comunidadvinos.model.SeguidoData;
import es.upm.sos.comunidadvinos.model.Usuario;

@Component
public class SeguidoDataModelAssembler extends RepresentationModelAssemblerSupport<Usuario, SeguidoData> {

    public SeguidoDataModelAssembler() {
        super(UsuarioController.class, SeguidoData.class);
    }

    @Override
    public SeguidoData toModel(Usuario usuario) {

        SeguidoData seguido = new SeguidoData();

        seguido.setId(usuario.getId());
        seguido.setNombre(usuario.getNombre());
        seguido.setCorreo(usuario.getCorreo());

        seguido.add(linkTo(methodOn(UsuarioController.class)
                .getUsuario(usuario.getId()))
                .withSelfRel());

        return seguido;
    }
}
