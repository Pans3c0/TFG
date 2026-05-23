package org.educastur.samuelepv59.todo_list.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.educastur.samuelepv59.todo_list.dtos.TareaRequest;
import org.educastur.samuelepv59.todo_list.dtos.TareaResponse;
import org.educastur.samuelepv59.todo_list.models.TaskPriority;
import org.educastur.samuelepv59.todo_list.services.TareaService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@RestController
@RequestMapping("/tareas")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Gestión de tareas principales")
public class TareaController {

    private final TareaService taskService;

    @Operation(summary = "Obtener tareas por autor", description = "Busca todas las tareas asociadas a un usuario específico")
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<TareaResponse> getUserTasks(
            @Parameter(description = "ID del autor de la tarea") @RequestParam Long authorId) {
        return taskService.getTasksByAuthor(authorId);
    }

    @Operation(summary = "Buscar tareas por prioridad", description = "Devuelve tareas que coincidan con la prioridad especificada")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/search/prioridad")
    public List<TareaResponse> getTasksByPriority(
            @Parameter(description = "Prioridad: ALTA, MEDIA, BAJA") @RequestParam TaskPriority prioridad) {
        return taskService.getTasksByPriority(prioridad);
    }

    @Operation(summary = "Buscar tareas por etiqueta", description = "Devuelve tareas asociadas a una etiqueta concreta")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/search/etiqueta")
    public List<TareaResponse> getTasksByTag(@Parameter(description = "ID de la etiqueta") @RequestParam Long tagId) {
        return taskService.getTasksByTag(tagId);
    }

    @Operation(summary = "Buscar tareas por título", description = "Busca tareas que contengan el texto especificado en el título")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/search")
    public List<TareaResponse> searchTareas(
            @Parameter(description = "Texto a buscar en el título") @RequestParam String titulo) {
        return taskService.searchByTitulo(titulo);
    }

    @Operation(summary = "Crear tarea", description = "Crea una nueva tarea y la asocia a un usuario")
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public TareaResponse createTask(
            @Valid @RequestBody TareaRequest request,
            @Parameter(description = "ID del creador de la tarea") @RequestParam Long authorId) {
        return taskService.crear(request, authorId);
    }

    @Operation(summary = "Actualizar tarea", description = "Modifica los campos principales de una tarea")
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public TareaResponse updateTask(
            @Parameter(description = "ID de la tarea") @PathVariable Long id,
            @Valid @RequestBody TareaRequest request) {
        return taskService.actualizar(id, request);
    }

    @Operation(summary = "Borrar tarea", description = "Elimina una tarea permanentemente")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public void deleteTask(@Parameter(description = "ID de la tarea a eliminar") @PathVariable Long id) {
        taskService.eliminar(id);
    }

    @Operation(summary = "Asignar etiqueta a tarea", description = "Vincula una etiqueta existente a una tarea")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/etiquetas/{tagId}")
    public TareaResponse anadirEtiqueta(
            @Parameter(description = "ID de la tarea") @PathVariable Long id,
            @Parameter(description = "ID de la etiqueta") @PathVariable Long tagId) {
        return taskService.anadirEtiqueta(id, tagId);
    }

    @Operation(summary = "Remover etiqueta de tarea", description = "Desvincula una etiqueta de una tarea sin borrar la etiqueta")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}/etiquetas/{tagId}")
    public TareaResponse quitarEtiqueta(
            @Parameter(description = "ID de la tarea") @PathVariable Long id,
            @Parameter(description = "ID de la etiqueta") @PathVariable Long tagId) {
        return taskService.quitarEtiqueta(id, tagId);
    }
}