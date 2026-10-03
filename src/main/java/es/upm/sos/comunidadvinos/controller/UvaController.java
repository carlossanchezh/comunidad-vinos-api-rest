package es.upm.sos.comunidadvinos.controller;

import es.upm.sos.comunidadvinos.exception.UvaNotFoundException;
import es.upm.sos.comunidadvinos.model.Uva;
import es.upm.sos.comunidadvinos.service.UvaService;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/uvas")
@AllArgsConstructor
public class UvaController {

    private final UvaService service;

    // OPERACION GET (OBTENER UVA)
    @GetMapping("/{id}")
    public ResponseEntity<Uva> getUva(@PathVariable Long id) {

        Uva uva = service.buscarPorId(id)
                .orElseThrow(() -> new UvaNotFoundException(id));

        uva.add(linkTo(methodOn(UvaController.class).getUva(id)).withSelfRel());

        return ResponseEntity.ok(uva);

    }

}
