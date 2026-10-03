package es.upm.sos.comunidadvinos.service;

import es.upm.sos.comunidadvinos.model.Seguimiento;
import es.upm.sos.comunidadvinos.model.SeguimientoId;
import es.upm.sos.comunidadvinos.model.Usuario;
import es.upm.sos.comunidadvinos.repository.SeguimientoRepository;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SeguimientoService {

    private final SeguimientoRepository repository;

    // SERVICE PARA OPERACION GET (OBTENER LISTADO DE VINOS DE UN USUARIO SEGUIDO)
    public boolean existePorSeguidorIdYSeguidoId(Long seguidorId, Long seguidoId) {
        return repository.existsBySeguidorIdAndSeguidoId(seguidorId, seguidoId);
    }

    // SERVICE PARA OPEREACION GET (OBTENER LISTADO DE USARIOS QUE SIGUE UN USUARIO
    // CON FILTRO)
    public Page<Seguimiento> buscarPorSeguidorId(Long usuarioId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Seguimiento> realaciones = repository.findBySeguidorId(usuarioId, pageable);

        return realaciones;
    }

    public Page<Seguimiento> buscarPorSeguidorIdYNombreSeguido(Long usuarioId, String filtro, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Seguimiento> realaciones = repository
                .findBySeguidorIdAndSeguidoNombreContainingIgnoreCase(usuarioId, filtro,
                        pageable);

        return realaciones;
    }
    // SERVICE PARA OPERACION DELETE (USUARIO ELIMINA A UN USURIO QUE SIGUE)

    public Optional<Seguimiento> buscarPorSeguidorIdSeguidoId(Long seguidorId, Long seguidoId) {
        return repository.findBySeguidorIdAndSeguidoId(seguidorId, seguidoId);
    }

    public void dejarDeSeguir(Seguimiento relacion) {

        repository.delete(relacion);
    }

    // SERVICE PARA OPERACION GET (OBTENER RECOMENDACION DE USUARIO)
    public List<Seguimiento> buscarPorSeguidorIdList(Long usuarioId) {

        List<Seguimiento> realaciones = repository.findBySeguidorId(usuarioId);

        return realaciones;
    }

    // SERVICE PARA OPERACION POST (USUARIO SOLICITA SEGUIR A OTRO)
    public void crearSeguimiento(SeguimientoId seguimientoId, Usuario seguidor, Usuario seguido) {

        // crear clave primaria compuesta
        seguimientoId.setSeguidorId(seguidor.getId());
        seguimientoId.setSeguidoId(seguido.getId());

        // crear relacion
        Seguimiento relacion = new Seguimiento();
        relacion.setId(seguimientoId);
        relacion.setSeguidor(seguidor);
        relacion.setSeguido(seguido);
        relacion.setFechaSolicitud(LocalDate.now());

        repository.save(relacion);
    }

}
