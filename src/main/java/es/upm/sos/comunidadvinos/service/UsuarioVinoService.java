package es.upm.sos.comunidadvinos.service;

import es.upm.sos.comunidadvinos.model.Usuario;
import es.upm.sos.comunidadvinos.model.Vino;
import es.upm.sos.comunidadvinos.model.UsuarioVinoId;
import es.upm.sos.comunidadvinos.model.UsuarioVino;
import es.upm.sos.comunidadvinos.repository.UsuarioVinoRepository;
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
public class UsuarioVinoService {

    private final UsuarioVinoRepository repository;

    // SERVICE PARA LA OPERACION POST (AÑADIR VINO A LISTA DE USUARIO)
    public boolean existePorUsuarioIdYVinoId(Long usuarioId, Long vinoId) {
        return repository.existsByUsuarioIdAndVinoId(usuarioId, vinoId);
    }

    public void asociarUsuarioVino(UsuarioVinoId usuarioVinoId, Usuario usuario, Vino vino, Integer puntuacion) {

        usuarioVinoId.setUsuarioId(usuario.getId());
        usuarioVinoId.setVinoId(vino.getId());

        UsuarioVino relacion = new UsuarioVino();
        relacion.setId(usuarioVinoId);
        relacion.setUsuario(usuario);
        relacion.setVino(vino);
        relacion.setPuntuacion(puntuacion);
        relacion.setFechaAnadido(LocalDate.now());

        repository.save(relacion);
    }

    // SERVICE PARA LA OPERACION GET (OBTENERLISTA DE VINOS DE USUARIO CON FILTROS)
    public Page<UsuarioVino> obtenerVinosDeUsuario(
            Long usuarioId,
            LocalDate fechaDesde,
            LocalDate fechaHasta,
            String tipo,
            String origen,
            Integer anada,
            String bodega,
            String uva,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<UsuarioVino> relaciones = repository.buscarConFiltros(usuarioId, fechaDesde, fechaHasta, tipo, origen,
                anada, bodega, uva, pageable);

        return relaciones;

    }

    // SERVICE PARA LA OPERACION GET(OBTENER LISTA VINOS DE UN USUARIO SEGUIDO CON
    // FILTROS)
    public Page<UsuarioVino> obtenerVinosDeUsuarioSeguido(
            Long seguidoId,
            Long seguidorId,
            LocalDate fechaDesde,
            LocalDate fechaHasta,
            String tipo,
            String origen,
            Integer anada,
            String bodega,
            String uva,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<UsuarioVino> relaciones = repository.buscarVinosDeUsuarioSeguidoConFiltros(seguidoId, seguidorId,
                fechaDesde, fechaHasta, tipo, origen, anada, bodega, uva, pageable);

        return relaciones;
    }

    // SERVICE PARA LA OPERACION GET (OBTENER RECOMENDACIOENS DE USUARIO)
    public List<UsuarioVino> obtenerUltimosVinosAñadidos(Long usuarioId) {
        List<UsuarioVino> list = repository.findByUsuarioIdOrderByFechaAnadidoDesc(usuarioId);
        return list;
    }

    public List<UsuarioVino> obtenerTopVinosPorPuntuacion(Long usuarioId) {
        List<UsuarioVino> list = repository.findByUsuarioIdOrderByPuntuacionDesc(usuarioId);
        return list;
    }

    // SERVICE PARA LA OPERACION GET (OBTENER ESTADISTICAS USUARIO)
    public Double calcularPuntuacionMedia(
            Long usuarioId,
            LocalDate fechaDesde,
            LocalDate fechaHasta,
            String tipo,
            String origen,
            Integer anada,
            String bodega,
            String uva) {

        Double media = repository.calcularPuntuacionMediaConFiltros(
                usuarioId, fechaDesde, fechaHasta, tipo, origen, anada, bodega, uva);

        if (media != null) {
            return media;
        } else {
            return 0.0;
        }
    }

    // SERVICE PARA LA OPERACION PUT (CAMBIAR PUNTUACION DE UN VINO DE LISTA
    // USUARIO)
    public void modificarPuntuacion(UsuarioVino relacion, Integer nuevaPuntuacion) {

        relacion.setPuntuacion(nuevaPuntuacion);
        repository.save(relacion);
    }

    // SERVICE PARA LA OPERACION DELETE (ELIMINAR VINO DE LISTA DE USUARIO)
    public Optional<UsuarioVino> buscarPorUsuarioIdAndVinoId(Long usuarioId, Long vinoId) {
        return repository.findByUsuarioIdAndVinoId(usuarioId, vinoId);
    }

    public void eliminarUsuarioVino(UsuarioVino relacion) {

        repository.delete(relacion);
    }

}
