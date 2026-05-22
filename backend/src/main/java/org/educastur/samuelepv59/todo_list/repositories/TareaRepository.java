package org.educastur.samuelepv59.todo_list.repositories;

import org.educastur.samuelepv59.todo_list.models.Tarea;
import org.educastur.samuelepv59.todo_list.models.TaskPriority;
import org.educastur.samuelepv59.todo_list.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {

    List<Tarea> findByAutor(Usuario autor);

    List<Tarea> findByTituloContaining(String titulo);

    List<Tarea> findByAutorId(Long id);

    List<Tarea> findByPrioridad(TaskPriority prioridad);

    List<Tarea> findByEtiquetasId(Long id);

    long countByCompletadaTrue();

    long countByCompletadaFalse();
}