package com.kanban.ai.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Long id) {
        super("Tarefa não encontrada com id: " + id);
    }
}
