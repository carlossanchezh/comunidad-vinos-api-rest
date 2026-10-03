package es.upm.sos.comunidadvinos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import es.upm.sos.comunidadvinos.model.Usuario;
import java.util.*;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsById(Long Id);

    Optional<Usuario> findById(Long Id);

    Page<Usuario> findByNombreContainingIgnoreCase(String nombreUsuario, Pageable pageable);

    Page<Usuario> findAll(Pageable pageable);

    boolean existsByCorreo(String correo);

}
