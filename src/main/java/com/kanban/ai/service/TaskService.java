package com.kanban.ai.service;

import com.kanban.ai.dto.TaskRequest;
import com.kanban.ai.dto.TaskResponse;
import com.kanban.ai.entity.Status;
import com.kanban.ai.entity.Task;
import com.kanban.ai.exception.TaskNotFoundException;
import com.kanban.ai.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskResponse create(TaskRequest request) {
        Task task = new Task();
        applyRequest(task, request);
        if (task.getStatus() == null) task.setStatus(Status.TODO);
        return TaskResponse.from(taskRepository.save(task));
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> findAll() {
        return taskRepository.findAll().stream()
                .map(TaskResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public TaskResponse update(Long id, TaskRequest request) {
        Task task = findById(id);
        applyRequest(task, request);
        return TaskResponse.from(taskRepository.save(task));
    }

    public void delete(Long id) {
        taskRepository.delete(findById(id));
    }

    private void applyRequest(Task task, TaskRequest request) {
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setDeadline(request.deadline());
        task.setPriority(request.priority());
        if (request.status() != null) task.setStatus(request.status());
        if (request.labels() != null) task.setLabels(request.labels());
    }
}
