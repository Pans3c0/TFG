package org.educastur.samuelepv59.todo_list.dtos;

import org.educastur.samuelepv59.todo_list.models.Tarea;
import org.educastur.samuelepv59.todo_list.models.TaskPriority;
import org.educastur.samuelepv59.todo_list.models.TaskStatus;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para mostrar tareas completas con toda su info al frontend.
 * Incluye fecha, prioridad, categoría y etiquetas anidadas para no hacer múltiples requests.
 */
public record TareaResponse(
                Long id,
                String titulo,
                String descripcion,
                boolean completada,
                LocalDateTime fechaCreacion, // El frontend necesita saber cuándo se creó
                LocalDateTime fechaLimite, // Mostrar fecha límite en el calendario/listado
                TaskPriority prioridad,
                TaskStatus estado,
                CategoriaResponse categoria, // Se envía el DTO de categoría, no solo el ID
                List<EtiquetaResponse> etiquetas, // Se mapean todas las etiquetas para evitar otra query
                UsuarioResponse autor // Se envía toda la info del usuario que creó la tarea
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