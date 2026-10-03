package es.upm.sos.comunidadvinos.model;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UvaPorcentajeData extends RepresentationModel<UvaPorcentajeData> {

    private Long id;
    private String nombre;
    private String descripcion;
    private Integer porcentaje;

}
