package org.educastur.samuelepv59.todo_list.dtos;

/**
 * DTO para el dashboard principal.
 * Se envía solo los números que el frontend necesita para mostrar stats.
 */
public record DashboardResponse(
        long totalTasks,
        long completedTasks,
        long pendingTasks
) {}
