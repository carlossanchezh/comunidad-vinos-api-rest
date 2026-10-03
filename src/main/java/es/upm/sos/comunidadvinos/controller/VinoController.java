package es.upm.sos.comunidadvinos.controller;

import es.upm.sos.comunidadvinos.exception.VinoNotFoundException;

import es.upm.sos.comunidadvinos.model.UvaPorcentajeData;
import es.upm.sos.comunidadvinos.model.Vino;
import es.upm.sos.comunidadvinos.model.VinoUva;
import es.upm.sos.comunidadvinos.service.VinoService;
import es.upm.sos.comunidadvinos.service.VinoUvaService;
import lombok.AllArgsConstructor;
import es.upm.sos.comunidadvinos.assembler.UvaPorcentajeDataModelAssembler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.*;

@RestController
@RequestMapping("/vinos")
@AllArgsConstructor
public class VinoController {

    private final VinoService service;

    private final VinoUvaService vinoUvaService;

    private UvaPorcentajeDataModelAssembler uvaPorcentajeDataModelAssembler;

    // OPERACION GET (OBTENER UN VINO)
    @GetMapping("/{id}")
    public ResponseEntity<Vino> getVino(@PathVariable Long id) {

        Vino vino = service.buscarPorId(id)
                .orElseThrow(() -> new VinoNotFoundException(id));

        vino.add(linkTo(methodOn(VinoController.class).getVino(id)).withSelfRel());

        Set<UvaPorcentajeData> uvasPorcentajeConLinks = new LinkedHashSet<>();

        for (VinoUva vinoUva : vinoUvaService.buscarPorVinoId(id)) {

            uvasPorcentajeConLinks.add(uvaPorcentajeDataModelAssembler.toModel(vinoUva));
        }

        vino.setUvas(uvasPorcentajeConLinks);

        return ResponseEntity.ok(vino);
    }

}
