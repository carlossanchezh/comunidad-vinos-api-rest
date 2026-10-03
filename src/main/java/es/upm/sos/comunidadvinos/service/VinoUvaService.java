package es.upm.sos.comunidadvinos.service;

import es.upm.sos.comunidadvinos.model.VinoUva;
import es.upm.sos.comunidadvinos.repository.VinoUvaRepository;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class VinoUvaService {

    private final VinoUvaRepository repository;

    // SERVICE PARA OPERACION GET (OBTENER VINO)
    public List<VinoUva> buscarPorVinoId(Long vinoId) {
        return repository.findByVinoId(vinoId);
    }

}
