package org.educastur.samuelepv59.todo_list.services;

import lombok.RequiredArgsConstructor;
import org.educastur.samuelepv59.todo_list.dtos.TareaRequest;
import org.educastur.samuelepv59.todo_list.dtos.TareaResponse;
import org.educastur.samuelepv59.todo_list.models.Tarea;
import org.educastur.samuelepv59.todo_list.models.TaskPriority;
import org.educastur.samuelepv59.todo_list.models.TaskStatus;
import org.educastur.samuelepv59.todo_list.models.Usuario;
import org.educastur.samuelepv59.todo_list.models.Etiqueta;
import org.educastur.samuelepv59.todo_list.repositories.CategoriaRepository;
import org.educastur.samuelepv59.todo_list.repositories.TareaRepository;
import org.educastur.samuelepv59.todo_list.repositories.UsuarioRepository;
import org.educastur.samuelepv59.todo_list.repositories.EtiquetaRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TareaService {

    private final TareaRepository taskRepository;
    private final UsuarioRepository userRepository;
    private final CategoriaRepository categoryRepository;
    private final EtiquetaRepository tagRepository;

    // Listar tareas de un usuario concreto
    public List<TareaResponse> getTasksByAuthor(Long authorId) {
        Usuario autor = userRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return taskRepository.findByAutorId(authorId)
                .stream()
                .map(TareaResponse::of)
                .toList();
    }

    public List<TareaResponse> getTasksByPriority(TaskPriority prioridad) {
        return taskRepository.findByPrioridad(prioridad)
                .stream()
                .map(TareaResponse::of)
                .toList();
    }

    public List<TareaResponse> getTasksByTag(Long tagId) {
        return taskRepository.findByEtiquetasId(tagId)
                .stream()
                .map(TareaResponse::of)
                .toList();
    }

    public List<TareaResponse> searchByTitulo(String titulo) {
        return taskRepository.findByTituloContaining(titulo)
                .stream()
                .map(TareaResponse::of)
                .toList();
    }

    public TareaResponse crear(TareaRequest request, Long authorId) {
        // 1. Recuperamos las etiquetas de golpe (si vienen en la request)
        Set<Etiqueta> etiquetasAsignadas = new HashSet<>();
        if (request.getEtiquetasIds() != null && !request.getEtiquetasIds().isEmpty()) {
            // findAllById devuelve un Iterable, lo pasamos al Set
            tagRepository.findAllById(request.getEtiquetasIds()).forEach(etiquetasAsignadas::add);
        }

        // 2. Construimos la tarea
        Tarea tarea = Tarea.builder()
                .titulo(request.getTitulo())
                .descripcion(request.getDescripcion())
                .fechaLimite(request.getFechaLimite())
                .prioridad(request.getPrioridad() != null ? request.getPrioridad() : TaskPriority.MEDIA)
                .estado(request.getEstado() != null ? request.getEstado() : TaskStatus.PENDIENTE)
                .completada(false)
                .autor(userRepository.findById(authorId)
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado")))
                .categoria(categoryRepository.findById(request.getCategoriaId()).orElse(null))
                .etiquetas(etiquetasAsignadas) // Asignamos las etiquetas recuperadas
                .build();

        // 3. Guardamos y retornamos
        return TareaResponse.of(taskRepository.save(tarea));
    }

    public TareaResponse actualizar(Long id, TareaRequest request) {
        Tarea tarea = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        tarea.setTitulo(request.getTitulo());
        tarea.setDescripcion(request.getDescripcion());
        tarea.setFechaLimite(request.getFechaLimite());
        if (request.getPrioridad() != null)
            tarea.setPrioridad(request.getPrioridad());
        if (request.getEstado() != null)
            tarea.setEstado(request.getEstado());
        if (request.getCategoriaId() != null) {
            tarea.setCategoria(categoryRepository.findById(request.getCategoriaId()).orElse(null));
        }

        return TareaResponse.of(taskRepository.save(tarea));
    }

    public void eliminar(Long id) {
        taskRepository.deleteById(id);
    }

    public TareaResponse anadirEtiqueta(Long taskId, Long tagId) {
        Tarea tarea = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
        Etiqueta etiqueta = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Etiqueta no encontrada"));

        tarea.getEtiquetas().add(etiqueta);
        return TareaResponse.of(taskRepository.save(tarea));
    }

    public TareaResponse quitarEtiqueta(Long taskId, Long tagId) {
        Tarea tarea = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
        Etiqueta etiqueta = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Etiqueta no encontrada"));

        tarea.getEtiquetas().remove(etiqueta);
        return TareaResponse.of(taskRepository.save(tarea));
    }
}