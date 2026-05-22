package org.educastur.samuelepv59.todo_list.controllers;

import lombok.RequiredArgsConstructor;
import org.educastur.samuelepv59.todo_list.dtos.DashboardResponse;
import org.educastur.samuelepv59.todo_list.repositories.TareaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Estadísticas y métricas generales del sistema")
public class DashboardController {

    private final TareaRepository taskRepository;

    @Operation(summary = "Obtener estadísticas del dashboard", description = "Devuelve los contadores totales de tareas, completadas y pendientes")
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public DashboardResponse getDashboardStats() {
        long total = taskRepository.count();
        long completed = taskRepository.countByCompletadaTrue();
        long pending = taskRepository.countByCompletadaFalse();
        return new DashboardResponse(total, completed, pending);
    }
}
