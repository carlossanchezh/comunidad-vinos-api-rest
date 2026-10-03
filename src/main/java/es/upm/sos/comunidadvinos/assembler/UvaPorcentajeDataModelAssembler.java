package es.upm.sos.comunidadvinos.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import es.upm.sos.comunidadvinos.controller.UvaController;
import es.upm.sos.comunidadvinos.model.Uva;
import es.upm.sos.comunidadvinos.model.UvaPorcentajeData;
import es.upm.sos.comunidadvinos.model.VinoUva;

@Component
public class UvaPorcentajeDataModelAssembler extends RepresentationModelAssemblerSupport<VinoUva, UvaPorcentajeData> {

    public UvaPorcentajeDataModelAssembler() {
        super(UvaController.class, UvaPorcentajeData.class);
    }

    @Override
    public UvaPorcentajeData toModel(VinoUva vinoUva) {
        Uva uva = vinoUva.getUva();

        UvaPorcentajeData uvaPorcentaje = new UvaPorcentajeData();
        uvaPorcentaje.setId(uva.getId());
        uvaPorcentaje.setNombre(uva.getNombre());
        uvaPorcentaje.setDescripcion(uva.getDescripcion());
        uvaPorcentaje.setPorcentaje(vinoUva.getPorcentaje());

        // Link a la uva
        uvaPorcentaje.add(linkTo(methodOn(UvaController.class).getUva(uva.getId())).withSelfRel());

        return uvaPorcentaje;
    }

}
