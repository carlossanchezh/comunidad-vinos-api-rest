package es.upm.sos.comunidadvinos.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeguidoRequestBody {

    @NotNull(message = "El ID del usuario a seguir es obligatorio")
    private Long seguidoId;
}
