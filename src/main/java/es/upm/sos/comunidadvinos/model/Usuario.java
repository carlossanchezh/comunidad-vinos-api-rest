package es.upm.sos.comunidadvinos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario extends RepresentationModel<Usuario> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY es autoincremeto en MySQL
    @Column(name = "id")
    private Long id;

    @NotNull(message = "El nombre es obligatorio y no puede ser null")
    @Column(name = "nombre")
    private String nombre;

    @NotNull(message = "La fecha de naciemiento es obligatoria y no puede ser null")
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @NotNull(message = "El correo es obligatorio y no puede ser null")
    @Column(name = "correo")
    private String correo;

}
