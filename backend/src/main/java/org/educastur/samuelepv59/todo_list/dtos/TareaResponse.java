package org.educastur.samuelepv59.todo_list.dtos;

import org.educastur.samuelepv59.todo_list.models.Tarea;
import org.educastur.samuelepv59.todo_list.models.TaskPriority;
import org.educastur.samuelepv59.todo_list.models.TaskStatus;
import java.time.LocalDateTime;
import java.util.List;

public record TareaResponse(
                Long id,
                String titulo,
                String descripcion,
                boolean completada,
                LocalDateTime fechaCreacion, // Añadido para que el front sepa cuándo se creó
                LocalDateTime fechaLimite, // Añadido para mostrar la fecha límite
                TaskPriority prioridad,
                TaskStatus estado,
                CategoriaResponse categoria, // Cambiado de String a su DTO
                List<EtiquetaResponse> etiquetas, // Añadido: lista de DTOs de etiquetas
                UsuarioResponse autor // Cambiado de String a su DTO
) {
        public static TareaResponse of(Tarea tarea) {
                return new TareaResponse(
                                tarea.getId(),
                                tarea.getTitulo(),
                                tarea.getDescripcion(),
                                tarea.isCompletada(),
                                tarea.getFechaCreacion(),
                                tarea.getFechaLimite(),
                                tarea.getPrioridad(),
                                tarea.getEstado(),
                                // Si la categoría es nula, pasamos null, si no, su DTO
                                tarea.getCategoria() != null ? CategoriaResponse.of(tarea.getCategoria()) : null,
                                // Mapeamos el Set de Tags de la entidad a una Lista de DTOs
                                tarea.getEtiquetas().stream()
                                                .map(EtiquetaResponse::of)
                                                .toList(),
                                // Mapeamos el autor a su DTO
                                UsuarioResponse.of(tarea.getAutor()));
        }
}