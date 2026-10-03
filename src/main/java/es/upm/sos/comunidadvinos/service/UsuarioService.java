package es.upm.sos.comunidadvinos.service;

import es.upm.sos.comunidadvinos.model.Usuario;
import es.upm.sos.comunidadvinos.repository.UsuarioRepository;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;

    // OPERACIONES SERVICE PARA COMPROBAR EXCEPCIONES
    public boolean existePorCorreo(String correo) {
        return repository.existsByCorreo(correo);
    }

    public boolean emailDuplicado(String correo) {
        return (existePorCorreo(correo));
    }

    public boolean usuarioEsMenorDeEdad(LocalDate fechaNacimiento) {
        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        return (edad < 18);
    }

    // SERVICE PARA LA OPERACION POST (CREAR USUARIO)
    public Usuario crearUsuario(Usuario usuario) {
        return repository.save(usuario);
    }

    // SERVICE PARA LA OPERACION GET (OBTENER UN USUARIO)
    public Optional<Usuario> buscarPorId(Long id) {
        return repository.findById(id);
    }

    // SERVICE PARA LA OPERACION GET (OBTENER LISTA DE USUARIOS PUDIENDO LIMITAR LOS
    // DATOS (PAGES) Y
    // PUDIENDO SER FILTRADA POR PATRON DE NOMBRE)
    public Page<Usuario> buscarUsuarios(String filtro, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if (filtro != null && !filtro.isEmpty()) {
            return repository.findByNombreContainingIgnoreCase(filtro, pageable);
        } else {
            return repository.findAll(pageable);
        }
    }

    // SERVICE PARA LA OPERACION PUT (CAMBIAR DATOS DE UN USUARIO)
    public Usuario actualizarUsuario(Usuario usuario, Usuario usuarioAct) {

        usuario.setNombre(usuarioAct.getNombre());
        usuario.setCorreo(usuarioAct.getCorreo());
        usuario.setFechaNacimiento(usuarioAct.getFechaNacimiento());

        return repository.save(usuario);
    }

    // SERVICE PARA LA OPERACION DELETE (BORRAR UN USUARIO)
    public boolean existePorId(Long id) {
        return repository.existsById(id);
    }

    public void eliminarUsuario(Long id) {
        repository.deleteById(id);
    }

}
