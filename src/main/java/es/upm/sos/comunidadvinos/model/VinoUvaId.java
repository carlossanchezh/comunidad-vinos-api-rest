package es.upm.sos.comunidadvinos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VinoUvaId {

    @NotNull(message = "")
    @Column(name = "vino_id")
    private Long vinoId;

    @NotNull(message = "")
    @Column(name = "uva_id")
    private Long uvaId;

}
