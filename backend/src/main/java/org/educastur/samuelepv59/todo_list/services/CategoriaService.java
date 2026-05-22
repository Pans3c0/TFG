package org.educastur.samuelepv59.todo_list.services;

import lombok.RequiredArgsConstructor;
import org.educastur.samuelepv59.todo_list.dtos.CategoriaRequest;
import org.educastur.samuelepv59.todo_list.dtos.CategoriaResponse;
import org.educastur.samuelepv59.todo_list.models.Categoria;
import org.educastur.samuelepv59.todo_list.repositories.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepository categoryRepository;

    public List<CategoriaResponse> listarTodas() {
        return categoryRepository.findAll().stream()
                .map(CategoriaResponse::of)
                .toList();
    }

    public CategoriaResponse obtenerPorId(Long id) {
        Categoria categoria = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        return CategoriaResponse.of(categoria);
    }

    public CategoriaResponse crear(CategoriaRequest request) {
        Categoria categoria = Categoria.builder()
                .nombre(request.getTitulo())
                .build();
        return CategoriaResponse.of(categoryRepository.save(categoria));
    }

    public CategoriaResponse actualizar(Long id, CategoriaRequest request) {
        Categoria categoria = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        categoria.setNombre(request.getTitulo());
        return CategoriaResponse.of(categoryRepository.save(categoria));
    }

    public void eliminar(Long id) {
        Categoria categoria = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        categoryRepository.delete(categoria);
    }
}
