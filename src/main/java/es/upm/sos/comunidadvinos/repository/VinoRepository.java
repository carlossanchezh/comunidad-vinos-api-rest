package es.upm.sos.comunidadvinos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import es.upm.sos.comunidadvinos.model.Vino;

import java.util.*;

@Repository
public interface VinoRepository extends JpaRepository<Vino, Long> {

    Optional<Vino> findById(Long id);

    boolean existsById(Long id);

}
