package es.upm.sos.comunidadvinos.model;

import org.springframework.hateoas.RepresentationModel;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "vino_uva")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VinoUva extends RepresentationModel<VinoUva> {

    @EmbeddedId
    private VinoUvaId id;

    @ManyToOne
    @MapsId("vinoId")
    @JoinColumn(name = "vino_id")
    private Vino vino;

    @ManyToOne
    @MapsId("uvaId")
    @JoinColumn(name = "uva_id")
    private Uva uva;

    @NotNull(message = "El porcentaje de uva en el vino es obligatorio")
    @Min(value = 1, message = "El porcentaje minimo es 1%")
    @Max(value = 100, message = "El procentje maximo es 100%")
    @Column(name = "porcentaje", nullable = false)
    private Integer porcentaje;
}
