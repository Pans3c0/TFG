package org.educastur.samuelepv59.todo_list.dtos;

import org.educastur.samuelepv59.todo_list.models.Categoria;

public record CategoriaResponse(Long id, String titulo) {
    public static CategoriaResponse of(Categoria categoria) {
        return new CategoriaResponse(categoria.getId(), categoria.getNombre());
    }
}
