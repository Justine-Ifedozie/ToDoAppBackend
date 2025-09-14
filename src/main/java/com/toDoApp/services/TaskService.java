package com.toDoApp.services;

import com.toDoApp.dtos.requests.TaskRequestDTO;
import com.toDoApp.dtos.responses.TaskResponseDTO;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    TaskResponseDTO createTask(TaskRequestDTO requestDTO);

    Optional<TaskResponseDTO> getTaskById(String taskId);

    List<TaskResponseDTO> getTasksByUserId(String userId);

    List<TaskResponseDTO> getTasksByCompletionStatus(boolean completed);

    Optional<TaskResponseDTO> updateTask(String taskId, TaskRequestDTO requestDTO);

    Optional<TaskResponseDTO> markTaskAsCompleted(String taskId);

    boolean deleteTask(String taskId);
}
