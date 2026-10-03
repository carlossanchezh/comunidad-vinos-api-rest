package es.upm.sos.comunidadvinos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudPendienteData extends RepresentationModel<SolicitudPendienteData> {

    private Long taskId;
    private Long seguidorId;
    private String seguidorNombre;
    private LocalDateTime fechaSolicitud;
}
