package es.upm.sos.comunidadvinos.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.upm.sos.comunidadvinos.model.VinoUva;
import es.upm.sos.comunidadvinos.model.VinoUvaId;

@Repository
public interface VinoUvaRepository extends JpaRepository<VinoUva, VinoUvaId> {

    List<VinoUva> findByVinoId(Long id);
}
