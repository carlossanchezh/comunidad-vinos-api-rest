package es.upm.sos.comunidadvinos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.upm.sos.comunidadvinos.model.Uva;

@Repository
public interface UvaRepository extends JpaRepository<Uva, Long> {

    Optional<Uva> findByNombre(String Nombre);
}
