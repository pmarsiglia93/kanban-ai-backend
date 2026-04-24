package com.kanban.ai.controller;

import com.kanban.ai.dto.PrioritySuggestion;
import com.kanban.ai.dto.TaskRequest;
import com.kanban.ai.dto.TaskResponse;
import com.kanban.ai.entity.Task;
import com.kanban.ai.service.AiPriorityService;
import com.kanban.ai.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final AiPriorityService aiPriorityService;

    public TaskController(TaskService taskService, AiPriorityService aiPriorityService) {
        this.taskService = taskService;
        this.aiPriorityService = aiPriorityService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse create(@Valid @RequestBody TaskRequest request) {
        return taskService.create(request);
    }

    @GetMapping
    public List<TaskResponse> findAll() {
        return taskService.findAll();
    }

    @PutMapping("/{id}")
    public TaskResponse update(@PathVariable Long id, @Valid @RequestBody TaskRequest request) {
        return taskService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }

    @PostMapping("/{id}/suggest-priority")
    public PrioritySuggestion suggestPriority(@PathVariable Long id) {
        Task task = taskService.findById(id);
        return aiPriorityService.suggestPriority(task);
    }
}
