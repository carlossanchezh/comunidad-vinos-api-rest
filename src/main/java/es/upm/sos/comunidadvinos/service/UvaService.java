package es.upm.sos.comunidadvinos.service;

import es.upm.sos.comunidadvinos.model.Uva;
import es.upm.sos.comunidadvinos.repository.UvaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UvaService {

    private UvaRepository repository;

    // SERVCIE PARA LA OPERACION GET (OBTENER UVA)
    public Optional<Uva> buscarPorId(Long id) {
        return repository.findById(id);
    }

}
