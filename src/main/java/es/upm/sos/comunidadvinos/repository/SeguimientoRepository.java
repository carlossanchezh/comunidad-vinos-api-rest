package es.upm.sos.comunidadvinos.repository;

import java.util.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.upm.sos.comunidadvinos.model.Seguimiento;
import es.upm.sos.comunidadvinos.model.SeguimientoId;

@Repository
public interface SeguimientoRepository extends JpaRepository<Seguimiento, SeguimientoId> {

        Optional<Seguimiento> findBySeguidorIdAndSeguidoId(Long seguidorId, Long seguidoId);

        boolean existsBySeguidorIdAndSeguidoId(Long seguidorId, Long seguidoId);

        Page<Seguimiento> findBySeguidorId(
                        Long usuarioId,
                        Pageable pageable);

        Page<Seguimiento> findBySeguidoId(Long seguidoId, Pageable pageable);

        List<Seguimiento> findBySeguidorId(Long usuarioId);

        Page<Seguimiento> findBySeguidorIdAndSeguidoNombreContainingIgnoreCase(Long seguidorId, String filtro,
                        Pageable pageable);

}
