package org.educastur.samuelepv59.todo_list.services;

import lombok.RequiredArgsConstructor;
import org.educastur.samuelepv59.todo_list.dtos.EtiquetaRequest;
import org.educastur.samuelepv59.todo_list.dtos.EtiquetaResponse;
import org.educastur.samuelepv59.todo_list.models.Etiqueta;
import org.educastur.samuelepv59.todo_list.repositories.EtiquetaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EtiquetaService {

    private final EtiquetaRepository tagRepository;

    public List<EtiquetaResponse> getAll() {
        return tagRepository.findAll().stream()
                .map(EtiquetaResponse::of)
                .toList();
    }

    public EtiquetaResponse obtenerPorId(Long id) {
        Etiqueta etiqueta = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Etiqueta no encontrado"));
        return EtiquetaResponse.of(etiqueta);
    }

    public EtiquetaResponse crear(EtiquetaRequest request) {
        Etiqueta etiqueta = Etiqueta.builder()
                .nombre(request.getText())
                .build();
        return EtiquetaResponse.of(tagRepository.save(etiqueta));
    }

    public EtiquetaResponse actualizar(Long id, EtiquetaRequest request) {
        Etiqueta etiqueta = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Etiqueta no encontrado"));
        etiqueta.setNombre(request.getText());
        return EtiquetaResponse.of(tagRepository.save(etiqueta));
    }

    public void eliminar(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new RuntimeException("Etiqueta no encontrado");
        }
        tagRepository.deleteById(id);
    }
}
