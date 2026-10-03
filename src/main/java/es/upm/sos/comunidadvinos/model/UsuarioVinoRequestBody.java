package es.upm.sos.comunidadvinos.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UsuarioVinoRequestBody {

    @NotNull(message = "El ID del vino es obligatorio")
    private Long vinoId;

    @NotNull(message = "La puntuación es obligatoria")
    @Min(value = 0, message = "La puntuación mínima es 0")
    @Max(value = 10, message = "La puntuación máxima es 10")
    private Integer puntuacion;

}
