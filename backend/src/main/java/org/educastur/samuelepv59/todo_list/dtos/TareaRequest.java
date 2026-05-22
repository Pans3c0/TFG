package org.educastur.samuelepv59.todo_list.dtos;

import jakarta.validation.constraints.*;
import lombok.*;
import org.educastur.samuelepv59.todo_list.models.TaskPriority;
import org.educastur.samuelepv59.todo_list.models.TaskStatus;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TareaRequest {

    @NotBlank(message = "El título no puede estar vacío")
    @Size(max = 100, message = "Título demasiado largo")
    private String titulo;

    private String descripcion;

    @Future(message = "La fecha límite debe ser en el futuro")
    private LocalDateTime fechaLimite;

    private TaskPriority prioridad;

    private TaskStatus estado;

    @NotNull(message = "La tarea debe pertenecer a una categoría")
    private Long categoriaId; // El front envía el ID, no el objeto completo
}