package es.upm.sos.comunidadvinos.repository;

import java.time.LocalDate;
import java.util.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.upm.sos.comunidadvinos.model.UsuarioVino;
import es.upm.sos.comunidadvinos.model.UsuarioVinoId;

@Repository
public interface UsuarioVinoRepository extends JpaRepository<UsuarioVino, UsuarioVinoId> {

        Optional<UsuarioVino> findByUsuarioIdAndVinoId(Long usuarioId, Long vinoId);

        boolean existsByUsuarioIdAndVinoId(Long usuarioId, Long vinoId);

        List<UsuarioVino> findByUsuarioIdOrderByFechaAnadidoDesc(Long usuarioId);

        List<UsuarioVino> findByUsuarioIdOrderByPuntuacionDesc(Long usuarioId);

        @Query("SELECT DISTINCT uv FROM UsuarioVino uv " +
                        "JOIN uv.vino v " +
                        "LEFT JOIN VinoUva vu ON vu.vino.id = v.id " +
                        "LEFT JOIN vu.uva u " +
                        "WHERE uv.usuario.id = :usuarioId " +
                        "AND (:fechaDesde IS NULL OR uv.fechaAnadido >= :fechaDesde) " +
                        "AND (:fechaHasta IS NULL OR uv.fechaAnadido <= :fechaHasta) " +
                        "AND (:tipo IS NULL OR v.tipo = :tipo) " +
                        "AND (:origen IS NULL OR v.origen = :origen) " +
                        "AND (:anada IS NULL OR v.anada = :anada) " +
                        "AND (:bodega IS NULL OR LOWER(v.bodega) LIKE LOWER(CONCAT('%', :bodega, '%'))) " +
                        "AND (:uva IS NULL OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', :uva, '%')))")
        Page<UsuarioVino> buscarConFiltros(
                        @Param("usuarioId") Long usuarioId,
                        @Param("fechaDesde") LocalDate fechaDesde,
                        @Param("fechaHasta") LocalDate fechaHasta,
                        @Param("tipo") String tipo,
                        @Param("origen") String origen,
                        @Param("anada") Integer anada,
                        @Param("bodega") String bodega,
                        @Param("uva") String uva,
                        Pageable pageable);

        @Query("SELECT DISTINCT uv FROM UsuarioVino uv " +
                        "JOIN uv.vino v " +
                        "LEFT JOIN VinoUva vu ON vu.vino.id = v.id " +
                        "LEFT JOIN vu.uva u " +
                        "WHERE uv.usuario.id = :seguidoId " +
                        "AND (:seguidorId) IN (SELECT s.seguidor.id FROM Seguimiento s WHERE s.seguido.id = :seguidoId) "
                        +
                        "AND (:fechaDesde IS NULL OR uv.fechaAnadido >= :fechaDesde) " +
                        "AND (:fechaHasta IS NULL OR uv.fechaAnadido <= :fechaHasta) " +
                        "AND (:tipo IS NULL OR v.tipo = :tipo) " +
                        "AND (:origen IS NULL OR v.origen = :origen) " +
                        "AND (:anada IS NULL OR v.anada = :anada) " +
                        "AND (:bodega IS NULL OR LOWER(v.bodega) LIKE LOWER(CONCAT('%', :bodega, '%'))) " +
                        "AND (:uva IS NULL OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', :uva, '%')))")
        Page<UsuarioVino> buscarVinosDeUsuarioSeguidoConFiltros(
                        @Param("seguidoId") Long seguidoId,
                        @Param("seguidorId") Long seguidorId,
                        @Param("fechaDesde") LocalDate fechaDesde,
                        @Param("fechaHasta") LocalDate fechaHasta,
                        @Param("tipo") String tipo,
                        @Param("origen") String origen,
                        @Param("anada") Integer anada,
                        @Param("bodega") String bodega,
                        @Param("uva") String uva,
                        Pageable pageable);

        @Query("SELECT AVG(uv.puntuacion) FROM UsuarioVino uv " +
                        "JOIN uv.vino v " +
                        "LEFT JOIN VinoUva vu ON vu.vino.id = v.id " +
                        "LEFT JOIN vu.uva u " +
                        "WHERE uv.usuario.id = :usuarioId " +
                        "AND (:fechaDesde IS NULL OR uv.fechaAnadido >= :fechaDesde) " +
                        "AND (:fechaHasta IS NULL OR uv.fechaAnadido <= :fechaHasta) " +
                        "AND (:tipo IS NULL OR v.tipo = :tipo) " +
                        "AND (:origen IS NULL OR v.origen = :origen) " +
                        "AND (:anada IS NULL OR v.anada = :anada) " +
                        "AND (:bodega IS NULL OR LOWER(v.bodega) LIKE LOWER(CONCAT('%', :bodega, '%'))) " +
                        "AND (:uva IS NULL OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', :uva, '%')))")
        Double calcularPuntuacionMediaConFiltros(
                        @Param("usuarioId") Long usuarioId,
                        @Param("fechaDesde") LocalDate fechaDesde,
                        @Param("fechaHasta") LocalDate fechaHasta,
                        @Param("tipo") String tipo,
                        @Param("origen") String origen,
                        @Param("anada") Integer anada,
                        @Param("bodega") String bodega,
                        @Param("uva") String uva);

}
