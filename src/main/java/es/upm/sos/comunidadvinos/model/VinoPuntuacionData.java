package es.upm.sos.comunidadvinos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VinoPuntuacionData extends RepresentationModel<VinoPuntuacionData> {

    private Long id;
    private String nombre;
    private String bodega;
    private Integer anada;
    private String origen;
    private String tipo;
    private Integer puntuacion;
    private LocalDate fechaAnadido;

}
