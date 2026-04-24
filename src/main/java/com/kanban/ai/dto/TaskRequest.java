package com.kanban.ai.dto;

import com.kanban.ai.entity.Priority;
import com.kanban.ai.entity.Status;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.List;

public record TaskRequest(
        @NotBlank(message = "Título é obrigatório") String title,
        String description,
        LocalDate deadline,
        Priority priority,
        Status status,
        List<String> labels
) {}
