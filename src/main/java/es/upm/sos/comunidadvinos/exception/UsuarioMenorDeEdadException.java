package es.upm.sos.comunidadvinos.exception;

import java.time.LocalDate;

public class UsuarioMenorDeEdadException extends RuntimeException {

    public UsuarioMenorDeEdadException(LocalDate fechaNacimiento) {
        super("El usuario que intenta crear (con fecha de naciemiento " + fechaNacimiento
                + ") es menor de edad. Para poder registrar un usuario este debe tener mas de 18 años");
    }

}
