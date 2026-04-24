package com.kanban.ai.dto;

import com.kanban.ai.entity.Priority;

public record PrioritySuggestion(
        Priority priority,
        String justification,
        int confidenceScore
) {}
