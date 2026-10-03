package es.upm.sos.comunidadvinos.controller;

import es.upm.sos.comunidadvinos.assembler.SeguidoDataModelAssembler;
import es.upm.sos.comunidadvinos.assembler.SolicitudPendienteDataModelAssembler;
import es.upm.sos.comunidadvinos.assembler.UsuarioModelAssembler;
import es.upm.sos.comunidadvinos.assembler.VinoPuntuacionDataModelAssembler;

import es.upm.sos.comunidadvinos.exception.SeguimientoASiMismoException;
import es.upm.sos.comunidadvinos.exception.SeguimientoNotFoundException;
import es.upm.sos.comunidadvinos.exception.SeguimientoYaExisteException;
import es.upm.sos.comunidadvinos.exception.SolicitudEstadoNoValidoActualizarException;
import es.upm.sos.comunidadvinos.exception.SolicitudNoPendienteException;
import es.upm.sos.comunidadvinos.exception.UsuarioMenorDeEdadException;
import es.upm.sos.comunidadvinos.exception.UsuarioNotFoundException;
import es.upm.sos.comunidadvinos.exception.UsuarioVinoNotFoundException;
import es.upm.sos.comunidadvinos.exception.UsuarioVinoYaExisteException;
import es.upm.sos.comunidadvinos.exception.UsuarioYaExisteException;
import es.upm.sos.comunidadvinos.exception.VinoNotFoundException;

import es.upm.sos.comunidadvinos.model.EstadisticasData;
import es.upm.sos.comunidadvinos.model.EstadoTarea;
import es.upm.sos.comunidadvinos.model.RecomendacionesData;
import es.upm.sos.comunidadvinos.model.SeguidoData;
import es.upm.sos.comunidadvinos.model.SeguidoRequestBody;
import es.upm.sos.comunidadvinos.model.Seguimiento;
import es.upm.sos.comunidadvinos.model.SeguimientoId;
import es.upm.sos.comunidadvinos.model.SolicitudPendienteData;
import es.upm.sos.comunidadvinos.model.TareaSeguimiento;
import es.upm.sos.comunidadvinos.model.Usuario;
import es.upm.sos.comunidadvinos.model.UsuarioVino;
import es.upm.sos.comunidadvinos.model.UsuarioVinoId;
import es.upm.sos.comunidadvinos.model.UsuarioVinoRequestBody;
import es.upm.sos.comunidadvinos.model.Vino;
import es.upm.sos.comunidadvinos.model.VinoPuntuacionData;

import es.upm.sos.comunidadvinos.service.SeguimientoService;
import es.upm.sos.comunidadvinos.service.TareaSeguimientoService;
import es.upm.sos.comunidadvinos.service.UsuarioService;
import es.upm.sos.comunidadvinos.service.UsuarioVinoService;
import es.upm.sos.comunidadvinos.service.VinoService;

import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedResourcesAssembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;

@RestController
@RequestMapping("/usuarios")
@XmlRootElement
@AllArgsConstructor
public class UsuarioController {

    private final UsuarioService service;

    private PagedResourcesAssembler<Usuario> pagedResourcesAssembler;
    private UsuarioModelAssembler usuarioModelAssembler;

    private final VinoService vinoService;

    private final UsuarioVinoService usuarioVinoService;

    private PagedResourcesAssembler<VinoPuntuacionData> vinoPuntuacionDataPagedResourcesAssembler;
    private VinoPuntuacionDataModelAssembler vinoPuntuacionDataModelAssembler;

    private final SeguimientoService seguimientoService;

    private PagedResourcesAssembler<SeguidoData> seguidoDataPagedResourcesAssembler;
    private SeguidoDataModelAssembler seguidoDataModelAssembler;

    private final TareaSeguimientoService tareaSeguimientoService;

    private PagedResourcesAssembler<SolicitudPendienteData> solicitudPendienteDataPagedResourcesAssembler;
    private SolicitudPendienteDataModelAssembler solicitudSeguimientoModelAssembler;

    // -----------------------------------------------
    // OPERACIONES RELACIONADAS CON EL RECURSO USUARIO
    // -----------------------------------------------

    // OPERACION POST (CREAR UN USUARIO)
    @PostMapping(consumes = { "application/json", "application/xml" })
    public ResponseEntity<Void> nuevoUsuario(@RequestBody Usuario nuevoUsuario) {

        if (service.usuarioEsMenorDeEdad(nuevoUsuario.getFechaNacimiento())) {
            throw new UsuarioMenorDeEdadException(nuevoUsuario.getFechaNacimiento());
        }

        if (service.emailDuplicado(nuevoUsuario.getCorreo())) {
            throw new UsuarioYaExisteException(nuevoUsuario.getCorreo());
        }

        Usuario usuario = service.crearUsuario(nuevoUsuario);

        return ResponseEntity
                .created(linkTo(methodOn(UsuarioController.class).getUsuario(usuario.getId())).toUri()).build();

    }

    // OPERACION GET (OBTENER UN USUARIO)
    @GetMapping(value = "/{id}", produces = { "application/json", "application/xml" })
    public ResponseEntity<Usuario> getUsuario(@PathVariable Long id) {

        Usuario usuario = service.buscarPorId(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));

        usuario.add(linkTo(methodOn(UsuarioController.class).getUsuario(id)).withSelfRel());

        return ResponseEntity.ok(usuario);
    }

    // OPERACION GET (OBTENER LISTA DE USUARIOS PUDIENDO LIMITAR LOS DATOS (PAGES) Y
    // PUDIENDO SER FILTRADA POR PATRON DE NOMBRE)
    @GetMapping(produces = { "application/json", "application/xml" })
    public ResponseEntity<PagedModel<Usuario>> getUsuarios(@RequestParam(required = false) String filtro,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {

        Page<Usuario> usuarios = service.buscarUsuarios(filtro, page, size);

        return ResponseEntity.ok(pagedResourcesAssembler.toModel(usuarios, usuarioModelAssembler));
    }

    // OPERACION PUT (ACTUALIZAR LOS DATOS DE UN USUARIO)
    @PutMapping(value = "/{id}", consumes = { "application/json", "application/xml" })
    public ResponseEntity<Void> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioAct) {

        Usuario usuario = service.buscarPorId(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));

        if (!usuario.getCorreo().equals(usuarioAct.getCorreo())) {
            if (service.emailDuplicado(usuarioAct.getCorreo())) {
                throw new UsuarioYaExisteException(usuarioAct.getCorreo());
            }
        }

        if (service.usuarioEsMenorDeEdad(usuarioAct.getFechaNacimiento())) {
            throw new UsuarioMenorDeEdadException(usuarioAct.getFechaNacimiento());
        }

        service.actualizarUsuario(usuario, usuarioAct);

        return ResponseEntity.noContent().build();

    }

    // OPERACION DELETE (BORRAR UN USUARIO)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {

        if (!service.existePorId(id)) {
            throw new UsuarioNotFoundException(id);
        }

        service.eliminarUsuario(id);

        return ResponseEntity.noContent().build();
    }

    // -----------------------------------------------------------------
    // OPERACIONES RELACIONADAS CON LA RELACION DE RECURSOS USUARIO VINO
    // -----------------------------------------------------------------

    // OPERACION POST (USUARIO AÑADE VINO A SU LISTA)
    @PostMapping(value = "/{usuarioId}/vinos", consumes = { "application/json", "application/xml" })
    public ResponseEntity<Void> crearRelacionUsuarioVino(@PathVariable Long usuarioId,
            @RequestBody UsuarioVinoRequestBody request) {

        Usuario usuario = service.buscarPorId(usuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException(usuarioId));

        Vino vino = vinoService.buscarPorId(request.getVinoId())
                .orElseThrow(() -> new VinoNotFoundException(request.getVinoId()));

        if (usuarioVinoService.existePorUsuarioIdYVinoId(usuarioId, request.getVinoId())) {
            throw new UsuarioVinoYaExisteException(usuario.getNombre(), vino.getNombre());
        }

        UsuarioVinoId usuarioVinoId = new UsuarioVinoId();

        usuarioVinoService.asociarUsuarioVino(usuarioVinoId, usuario, vino, request.getPuntuacion());

        return ResponseEntity.created(linkTo(methodOn(UsuarioController.class)
                .getVinosDeUsuario(usuarioId, null, null, null, null, null, null, null, 0, 5))
                .toUri()).build();
    }

    // OPERACION GET (OBTENR LISTA DE VINOS DE USUARIO PUDIENDO SER FILTRADA)
    @GetMapping(value = "/{usuarioId}/vinos", produces = { "application/json", "application/xml" })
    public ResponseEntity<PagedModel<EntityModel<VinoPuntuacionData>>> getVinosDeUsuario(
            @PathVariable Long usuarioId,
            @RequestParam(required = false) LocalDate fechaDesde,
            @RequestParam(required = false) LocalDate fechaHasta,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String origen,
            @RequestParam(required = false) Integer anada,
            @RequestParam(required = false) String bodega,
            @RequestParam(required = false) String uva,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        if (!service.existePorId(usuarioId)) {
            throw new UsuarioNotFoundException(usuarioId);
        }

        Page<UsuarioVino> relaciones = usuarioVinoService.obtenerVinosDeUsuario(usuarioId, fechaDesde, fechaHasta, tipo,
                origen,
                anada, bodega, uva, page, size);

        List<VinoPuntuacionData> vinosList = new ArrayList<>();
        for (UsuarioVino relacion : relaciones.getContent()) {
            VinoPuntuacionData vinoPuntuacion = vinoPuntuacionDataModelAssembler.toModel(relacion);
            vinosList.add(vinoPuntuacion);
        }

        vinosList.sort(Comparator.comparing(VinoPuntuacionData::getId));

        Page<VinoPuntuacionData> vinosPage = new PageImpl<>(vinosList, relaciones.getPageable(),
                relaciones.getTotalElements());

        return ResponseEntity.ok(vinoPuntuacionDataPagedResourcesAssembler.toModel(vinosPage));
    }

    // OPERACION PUT (USUARIO PUEDE MODIFICAR LA PUNTUACION E UN VINO DE SU LISTA)
    @PutMapping(value = "/{usuarioId}/vinos/{vinoId}", consumes = { "application/json", "application/xml" })
    public ResponseEntity<Void> modificarPuntuacion(
            @PathVariable Long usuarioId,
            @PathVariable Long vinoId,
            @RequestBody UsuarioVino relacionActualizado) {

        if (!service.existePorId(usuarioId)) {
            throw new UsuarioNotFoundException(usuarioId);
        }

        if (!vinoService.existePorId(vinoId)) {
            throw new VinoNotFoundException(vinoId);
        }

        UsuarioVino relacion = usuarioVinoService.buscarPorUsuarioIdAndVinoId(usuarioId, vinoId)
                .orElseThrow(() -> new UsuarioVinoNotFoundException(usuarioId, vinoId));

        usuarioVinoService.modificarPuntuacion(relacion, relacionActualizado.getPuntuacion());

        return ResponseEntity.noContent().build();
    }

    // OPERACION DELETE (USUARIO ELIMINA VINO DE SU LISTA)
    @DeleteMapping("/{usuarioId}/vinos/{vinoId}")
    public ResponseEntity<Void> eliminarUsuarioVino(@PathVariable Long usuarioId, @PathVariable Long vinoId) {

        if (!service.existePorId(usuarioId)) {
            throw new UsuarioNotFoundException(usuarioId);
        }

        if (!vinoService.existePorId(vinoId)) {
            throw new VinoNotFoundException(vinoId);
        }

        UsuarioVino relacion = usuarioVinoService.buscarPorUsuarioIdAndVinoId(usuarioId, vinoId)
                .orElseThrow(() -> new UsuarioVinoNotFoundException(usuarioId, vinoId));

        usuarioVinoService.eliminarUsuarioVino(relacion);

        return ResponseEntity.noContent().build();

    }

    // ----------------------------------------------------------------------------------
    // OPERACIONES RELACIONADAS CON LA RELACION DE RECURSOS USUARIO USUARIO
    // (SEGUIMIENTO)
    // ----------------------------------------------------------------------------------

    // OPEACION POST (USUARIO PUEDE SOLICITAR SEGUIR A OTRO) (ASINCRONA)
    @PostMapping(value = "/{seguidorId}/seguidos", consumes = { "application/json", "application/xml" })
    public ResponseEntity<TareaSeguimiento> solicitarSeguimiento(
            @PathVariable Long seguidorId,
            @RequestBody SeguidoRequestBody request) {

        Usuario seguidor = service.buscarPorId(seguidorId)
                .orElseThrow(() -> new UsuarioNotFoundException(seguidorId));
        Usuario seguido = service.buscarPorId(request.getSeguidoId())
                .orElseThrow(() -> new UsuarioNotFoundException(request.getSeguidoId()));

        if (seguidorId.equals(request.getSeguidoId())) {
            throw new SeguimientoASiMismoException(seguidorId);
        }

        if (seguimientoService.existePorSeguidorIdYSeguidoId(seguidorId, request.getSeguidoId())) {
            throw new SeguimientoYaExisteException(seguidorId, request.getSeguidoId());
        }

        TareaSeguimiento tarea = tareaSeguimientoService.crearTarea(
                seguidorId, seguidor.getNombre(),
                request.getSeguidoId(), seguido.getNombre());

        URI location = linkTo(methodOn(TareasController.class).getTarea(tarea.getTaskId())).toUri();

        tarea.add(linkTo(methodOn(TareasController.class).getTarea(tarea.getTaskId())).withSelfRel());

        return ResponseEntity.accepted().location(location).body(tarea);
    }

    // OPERACION GET (USUARIO PUEDE VER LISTA DE USUARIOS QUE SIGUE)
    @GetMapping(value = "/{usuarioId}/seguidos", produces = { "application/json", "application/xml" })
    public ResponseEntity<PagedModel<EntityModel<SeguidoData>>> getSeguidos(
            @PathVariable Long usuarioId,
            @RequestParam(required = false) String filtro,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        if (!service.existePorId(usuarioId)) {
            throw new UsuarioNotFoundException(usuarioId);
        }

        Page<Seguimiento> realciones;

        if (filtro != null && !filtro.isEmpty()) {
            realciones = seguimientoService.buscarPorSeguidorIdYNombreSeguido(usuarioId, filtro, page, size);
        } else {
            realciones = seguimientoService.buscarPorSeguidorId(usuarioId, page, size);
        }

        List<SeguidoData> seguidosL = new ArrayList<>();
        for (Seguimiento relacion : realciones.getContent()) {
            Usuario usuario = relacion.getSeguido();
            SeguidoData seguido = seguidoDataModelAssembler.toModel(usuario);
            seguidosL.add(seguido);
        }

        Page<SeguidoData> seguidos = new PageImpl<>(seguidosL, realciones.getPageable(), realciones.getTotalElements());

        return ResponseEntity.ok(seguidoDataPagedResourcesAssembler.toModel(seguidos));
    }

    // OPERACION GET (USUARIO PUEDE VER LA LISTA DE SOLICITUDES PNDIENTES)
    @GetMapping(value = "/{seguidoId}/solicitudes", produces = { "application/json", "application/xml" })
    public ResponseEntity<PagedModel<EntityModel<SolicitudPendienteData>>> getTareasPendientes(
            @PathVariable Long seguidoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        if (!service.existePorId(seguidoId)) {
            throw new UsuarioNotFoundException(seguidoId);
        }

        Page<TareaSeguimiento> tareas = tareaSeguimientoService.findTareasPendientesBySeguidoId(seguidoId, page, size);

        List<SolicitudPendienteData> solicitudesConLinks = new ArrayList<>();
        for (TareaSeguimiento tarea : tareas.getContent()) {

            SolicitudPendienteData solicitud = solicitudSeguimientoModelAssembler.toModel(tarea);

            solicitudesConLinks.add(solicitud);
        }

        Page<SolicitudPendienteData> solicitudesPage = new PageImpl<>(solicitudesConLinks, tareas.getPageable(),
                tareas.getTotalElements());

        return ResponseEntity.ok(solicitudPendienteDataPagedResourcesAssembler.toModel(solicitudesPage));
    }

    // OPERACION PUT (USUARIO PUEDE ACEPTAR SOLICITUD DE SEGUIMIENTO)
    @PutMapping("/{seguidoId}/solicitudes/{seguidorId}")
    public ResponseEntity<Void> actualizarSolicitud(
            @PathVariable Long seguidoId,
            @PathVariable Long seguidorId,
            @RequestBody TareaSeguimiento tareaActualizada) {

        if (!service.existePorId(seguidoId)) {
            throw new UsuarioNotFoundException(seguidoId);
        }
        if (!service.existePorId(seguidorId)) {
            throw new UsuarioNotFoundException(seguidorId);
        }

        TareaSeguimiento tarea = tareaSeguimientoService
                .buscarPorSeguidorYSeguido(seguidorId, seguidoId)
                .orElseThrow(() -> new SeguimientoNotFoundException(seguidorId, seguidoId));

        if (tarea.getEstado() != EstadoTarea.PENDIENTE) {
            throw new SolicitudNoPendienteException(seguidoId, seguidorId);
        }

        if (tareaActualizada.getEstado().equals(EstadoTarea.ACEPTADA)) {

            Usuario seguidor = service.buscarPorId(seguidorId).get();
            Usuario seguido = service.buscarPorId(seguidoId).get();
            SeguimientoId id = new SeguimientoId();
            seguimientoService.crearSeguimiento(id, seguidor, seguido);

            String resultadoUri = "/api/v1/usuarios/" + seguidorId + "/seguidos";
            tareaSeguimientoService.completarTarea(tarea.getTaskId(), resultadoUri);

        } else if (tareaActualizada.getEstado().equals(EstadoTarea.RECHAZADA)) {
            tareaSeguimientoService.fallarTarea(tarea.getTaskId(), "Solicitud rechazada por el usuario");
        } else {
            throw new SolicitudEstadoNoValidoActualizarException(seguidoId, seguidorId);
        }

        return ResponseEntity.noContent().build();
    }

    // OPERACION DELETE (USUARIO PUEDE DEJAR DE SEGUIR A USUARIO QUE SIGA)
    @DeleteMapping("/{seguidorId}/seguidos/{seguidoId}")
    public ResponseEntity<Void> dejarSeguir(@PathVariable Long seguidorId, @PathVariable Long seguidoId) {
        if (!service.existePorId(seguidorId)) {
            throw new UsuarioNotFoundException(seguidorId);
        }

        if (!service.existePorId(seguidoId)) {
            throw new UsuarioNotFoundException(seguidoId);
        }

        if (seguidoId.equals(seguidorId)) {
            throw new SeguimientoASiMismoException(seguidoId);
        }

        Seguimiento relacion = seguimientoService.buscarPorSeguidorIdSeguidoId(seguidorId, seguidoId)
                .orElseThrow(() -> new SeguimientoNotFoundException(seguidorId, seguidoId));

        seguimientoService.dejarDeSeguir(relacion);

        return ResponseEntity.noContent().build();

    }

    // ----------------------------------------------------------------------------------
    // OPERACIONES QUE INVOLUCRAN TANTO LA RECLACION USUARIO VINO Y USUARIO USUARIO
    // ----------------------------------------------------------------------------------

    // OPERACION GET (USUARIO PUEDE VER LISTA DE VINOS DE USUARIO QUE SIGUE)
    @GetMapping(value = "/{seguidorId}/seguidos/{seguidoId}/vinos", produces = { "application/json",
            "application/xml" })
    public ResponseEntity<PagedModel<EntityModel<VinoPuntuacionData>>> getVinosDeUsuarioSeguido(
            @PathVariable Long seguidorId,
            @PathVariable Long seguidoId,
            @RequestParam(required = false) LocalDate fechaDesde,
            @RequestParam(required = false) LocalDate fechaHasta,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String origen,
            @RequestParam(required = false) Integer anada,
            @RequestParam(required = false) String bodega,
            @RequestParam(required = false) String uva,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        if (!service.existePorId(seguidorId)) {
            throw new UsuarioNotFoundException(seguidorId);
        }

        if (!service.existePorId(seguidoId)) {
            throw new UsuarioNotFoundException(seguidoId);
        }

        if (seguidoId.equals(seguidorId)) {
            throw new SeguimientoASiMismoException(seguidoId);
        }

        if (!seguimientoService.existePorSeguidorIdYSeguidoId(seguidorId, seguidoId)) {
            throw new SeguimientoNotFoundException(seguidorId, seguidoId);
        }

        Page<UsuarioVino> relaciones = usuarioVinoService.obtenerVinosDeUsuarioSeguido(
                seguidoId, seguidorId, fechaDesde, fechaHasta, tipo, origen, anada, bodega, uva, page, size);

        List<VinoPuntuacionData> vinosList = new ArrayList<>();
        for (UsuarioVino relacion : relaciones.getContent()) {
            VinoPuntuacionData vinoPuntuacion = vinoPuntuacionDataModelAssembler.toModel(relacion);
            vinosList.add(vinoPuntuacion);
        }

        vinosList.sort(Comparator.comparing(VinoPuntuacionData::getId));

        Page<VinoPuntuacionData> vinosPage = new PageImpl<>(vinosList, relaciones.getPageable(),
                relaciones.getTotalElements());

        return ResponseEntity
                .ok(vinoPuntuacionDataPagedResourcesAssembler.toModel(vinosPage));
    }

    // OPERACION GET (OBETENER RECOMENDACIONES DE USUARIO)
    @GetMapping(value = "/{id}/recomendaciones", produces = { "application/json", "application/xml" })
    public ResponseEntity<RecomendacionesData> getRecomendaciones(@PathVariable Long id) {

        Usuario usuario = service.buscarPorId(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));

        Usuario usuarioConLinks = usuarioModelAssembler.toModel(usuario);

        List<UsuarioVino> relacion1 = usuarioVinoService.obtenerUltimosVinosAñadidos(id);
        List<VinoPuntuacionData> ultimos5Vinos = new ArrayList<>();
        for (UsuarioVino relacion : relacion1) {
            VinoPuntuacionData vinoPuntuacion = vinoPuntuacionDataModelAssembler.toModel(relacion);
            ultimos5Vinos.add(vinoPuntuacion);
        }

        List<UsuarioVino> relacion2 = usuarioVinoService.obtenerTopVinosPorPuntuacion(id);
        List<VinoPuntuacionData> top5PuntuacionUsuario = new ArrayList<>();
        for (UsuarioVino relacion : relacion2) {
            VinoPuntuacionData vinoPuntuacion = vinoPuntuacionDataModelAssembler.toModel(relacion);
            top5PuntuacionUsuario.add(vinoPuntuacion);
        }

        List<Seguimiento> realcion3 = seguimientoService.buscarPorSeguidorIdList(id);
        List<Usuario> amigos = new ArrayList<>();
        for (Seguimiento relacion : realcion3) {

            Usuario seguido = relacion.getSeguido();
            if (seguimientoService.existePorSeguidorIdYSeguidoId(seguido.getId(), id)) {
                amigos.add(seguido);
            }

        }

        List<VinoPuntuacionData> vinosDeAmigos = new ArrayList<>();
        for (Usuario amigo : amigos) {

            List<UsuarioVino> vinosAmigo = usuarioVinoService.obtenerTopVinosPorPuntuacion(amigo.getId());
            for (UsuarioVino uv : vinosAmigo) {
                VinoPuntuacionData vinoData = vinoPuntuacionDataModelAssembler.toModel(uv);

                vinoData.add(linkTo(methodOn(UsuarioController.class).getUsuario(amigo.getId()))
                        .withRel("amigo"));

                vinosDeAmigos.add(vinoData);
            }
        }

        List<VinoPuntuacionData> top5MejoresVinosAmigos = new ArrayList<>(vinosDeAmigos);
        top5MejoresVinosAmigos.sort(Comparator.comparing(VinoPuntuacionData::getPuntuacion).reversed());

        List<VinoPuntuacionData> ultimos5VinosLimitada = new ArrayList<>();
        List<VinoPuntuacionData> top5PuntuacionUsuarioLimitada = new ArrayList<>();
        List<VinoPuntuacionData> top5MejoresVinosAmigosLimitada = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (i < ultimos5Vinos.size()) {
                ultimos5VinosLimitada.add(ultimos5Vinos.get(i));
            }
            if (i < top5PuntuacionUsuario.size()) {
                top5PuntuacionUsuarioLimitada.add(top5PuntuacionUsuario.get(i));
            }
            if (i < top5MejoresVinosAmigos.size()) {
                top5MejoresVinosAmigosLimitada.add(top5MejoresVinosAmigos.get(i));
            }
        }

        RecomendacionesData recomendaciones = new RecomendacionesData(
                usuarioConLinks,
                ultimos5VinosLimitada,
                top5PuntuacionUsuarioLimitada,
                top5MejoresVinosAmigosLimitada);

        recomendaciones.add(linkTo(methodOn(UsuarioController.class).getRecomendaciones(id)).withSelfRel());

        return ResponseEntity.ok(recomendaciones);
    }

    // OPERACION GET (OBETENER ESTADISTICAS DE USUARIO)
    @GetMapping(value = "/{id}/estadisticas", produces = { "application/json", "application/xml" })
    public ResponseEntity<EstadisticasData> getEstadisticas(
            @PathVariable Long id,
            @RequestParam(required = false) LocalDate fechaDesde,
            @RequestParam(required = false) LocalDate fechaHasta,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String origen,
            @RequestParam(required = false) Integer anada,
            @RequestParam(required = false) String bodega,
            @RequestParam(required = false) String uva) {

        Usuario usuario = service.buscarPorId(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));

        Double media = usuarioVinoService.calcularPuntuacionMedia(id, fechaDesde, fechaHasta, tipo, origen, anada,
                bodega, uva);

        EstadisticasData estadisticas = new EstadisticasData(usuario.getId(), usuario.getNombre(), media);

        estadisticas.add(linkTo(methodOn(UsuarioController.class).getEstadisticas(
                id, fechaDesde, fechaHasta, tipo, origen, anada, bodega, uva))
                .withSelfRel());

        estadisticas.add(linkTo(methodOn(UsuarioController.class).getUsuario(id))
                .withRel("usuario"));

        estadisticas.add(linkTo(methodOn(UsuarioController.class).getVinosDeUsuario(
                id, null, null, null, null, null, null, null, 0, 5))
                .withRel("vinos"));

        return ResponseEntity.ok(estadisticas);
    }

}
