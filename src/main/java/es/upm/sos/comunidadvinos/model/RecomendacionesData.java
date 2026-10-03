package es.upm.sos.comunidadvinos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecomendacionesData extends RepresentationModel<RecomendacionesData> {

    private Usuario usuario;
    private List<VinoPuntuacionData> ultimosVinosAñadidos;
    private List<VinoPuntuacionData> vinosMejorPuntuadosUsuario;
    private List<VinoPuntuacionData> vinosMejorPuntuadosAmigos;
}
