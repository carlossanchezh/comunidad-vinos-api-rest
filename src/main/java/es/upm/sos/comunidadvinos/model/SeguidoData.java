package es.upm.sos.comunidadvinos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeguidoData extends RepresentationModel<SeguidoData> {

    private Long id;
    private String nombre;
    private String correo;

}
