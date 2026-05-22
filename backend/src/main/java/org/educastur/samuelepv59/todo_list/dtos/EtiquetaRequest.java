package org.educastur.samuelepv59.todo_list.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class EtiquetaRequest {
    @NotBlank(message = "El texto del etiqueta es obligatorio")
    private String text;
}
