package es.upm.sos.comunidadvinos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TareaSeguimiento extends RepresentationModel<TareaSeguimiento> {
    private Long taskId;
    private Long seguidorId;
    private String seguidorNombre;
    private Long seguidoId;
    private String seguidoNombre;
    private EstadoTarea estado;
    private String mensaje;
    private LocalDateTime fechaSolicitud;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime fechaRespuesta;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String resultadoUri;
}
