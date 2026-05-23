package org.educastur.samuelepv59.todo_list.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear/editar una categoría.
 * El backend genera el ID, nosotros solo recibimos el nombre.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CategoriaRequest {

    @NotBlank(message = "La categoria debe llevar nombre")
    @Size(min = 3, max = 50, message = "La categoria debe tener entre 3 y 50")
    private String titulo;
}
