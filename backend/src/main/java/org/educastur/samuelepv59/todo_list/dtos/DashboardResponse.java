package org.educastur.samuelepv59.todo_list.dtos;

public record DashboardResponse(
        long totalTasks,
        long completedTasks,
        long pendingTasks
) {}
