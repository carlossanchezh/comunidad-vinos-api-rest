package es.upm.sos.comunidadvinos.model;

import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "seguimientos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seguimiento extends RepresentationModel<Seguimiento> {

    @EmbeddedId
    private SeguimientoId id;

    @ManyToOne
    @MapsId("seguidorId")
    @JoinColumn(name = "seguidor_id", nullable = false)
    private Usuario seguidor;

    @ManyToOne
    @MapsId("seguidoId")
    @JoinColumn(name = "seguido_id", nullable = false)
    private Usuario seguido;

    @NotNull(message = "La fecha en la que se solicitó seguir es obligatoria")
    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDate fechaSolicitud;

}
