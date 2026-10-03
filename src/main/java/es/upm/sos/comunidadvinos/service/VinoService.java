package es.upm.sos.comunidadvinos.service;

import java.util.Optional;

import es.upm.sos.comunidadvinos.model.Vino;
import es.upm.sos.comunidadvinos.repository.VinoRepository;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class VinoService {

    private VinoRepository repository;

    // SERVICE PARA LA OPERACION GET (OBTENER UN VINO)
    public Optional<Vino> buscarPorId(Long id) {
        return repository.findById(id);
    }

    // SERVICE PARA LA OPERACION DELETE (USUARIO ELIMINA VINO DE SU LISTA)
    public boolean existePorId(Long id) {
        return repository.existsById(id);
    }
}
