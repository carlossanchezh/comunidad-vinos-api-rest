package es.upm.sos.comunidadvinos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "vinos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vino extends RepresentationModel<Vino> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "El nombre es obligatorio")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull(message = "La bodega es obligatoria")
    @Column(name = "bodega", nullable = false)
    private String bodega;

    @NotNull(message = "El año es obligatorio")
    @Column(name = "anada", nullable = false)
    private Integer anada;

    @NotNull(message = "El origen es obligatorio")
    @Column(name = "origen", nullable = false)
    private String origen;

    @NotNull(message = "El tipo de vino es obligatorio")
    @Column(name = "tipo", nullable = false)
    private String tipo;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<UvaPorcentajeData> uvas = new HashSet<>();

}
