package com.kanban.ai.dto;

import com.kanban.ai.entity.Priority;
import com.kanban.ai.entity.Status;
import com.kanban.ai.entity.Task;

import java.time.LocalDate;
import java.util.List;

public record TaskResponse(
        Long id,
        String title,
        String description,
        LocalDate deadline,
        Priority priority,
        Status status,
        List<String> labels
) {
    public static TaskResponse from(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDeadline(),
                task.getPriority(),
                task.getStatus(),
                task.getLabels()
        );
    }
}
