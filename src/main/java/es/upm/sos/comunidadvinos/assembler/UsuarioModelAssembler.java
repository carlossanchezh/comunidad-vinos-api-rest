package es.upm.sos.comunidadvinos.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import es.upm.sos.comunidadvinos.controller.UsuarioController;
import es.upm.sos.comunidadvinos.model.Usuario;

@Component
public class UsuarioModelAssembler extends RepresentationModelAssemblerSupport<Usuario, Usuario> {

    public UsuarioModelAssembler() {
        super(UsuarioController.class, Usuario.class);
    }

    @Override
    public Usuario toModel(Usuario usuario) {
        usuario.add(linkTo(methodOn(UsuarioController.class).getUsuario(usuario.getId())).withSelfRel());
        return usuario;
    }

}
