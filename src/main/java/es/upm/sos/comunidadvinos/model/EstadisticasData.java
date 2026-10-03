package es.upm.sos.comunidadvinos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticasData extends RepresentationModel<EstadisticasData> {

    private Long usuarioId;
    private String nombreUsuario;
    private Double puntuacionMedia;

}
