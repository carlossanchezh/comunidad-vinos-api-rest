package es.upm.sos.comunidadvinos.model;

import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "usuario_vino")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioVino extends RepresentationModel<UsuarioVino> {

    @EmbeddedId
    private UsuarioVinoId id;

    @ManyToOne
    @MapsId("usuarioId")
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @MapsId("vinoId")
    @JoinColumn(name = "vino_id", nullable = false)
    private Vino vino;

    @NotNull(message = "La puntuacion es obligatoria")
    @Min(value = 0, message = "La puntuacion minima es 0")
    @Max(value = 10, message = "La puntuacion minima es 10")
    @Column(name = "puntuacion", nullable = false)
    private Integer puntuacion;

    @Column(name = "fecha_anadido")
    private LocalDate fechaAnadido;

}
