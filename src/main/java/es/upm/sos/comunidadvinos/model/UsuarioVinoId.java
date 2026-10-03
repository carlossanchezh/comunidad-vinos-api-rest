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
public class UsuarioVinoId {

    @NotNull(message = "El id de usuario es obligatorio")
    @Column(name = "usuario_id")
    private Long usuarioId;

    @NotNull(message = "El id de vino es obligatorio")
    @Column(name = "vino_id")
    private Long vinoId;

}
