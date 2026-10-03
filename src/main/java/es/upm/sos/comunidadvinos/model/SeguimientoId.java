package es.upm.sos.comunidadvinos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeguimientoId {

    @NotNull(message = "El id del seguidor es obligatorio")
    @Column(name = "seguidor_id")
    private Long seguidorId;

    @NotNull(message = "El id de vino es obligatorio")
    @Column(name = "seguido_id")
    private Long seguidoId;

}
