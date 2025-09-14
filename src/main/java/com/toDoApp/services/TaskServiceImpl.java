package com.toDoApp.services;


import com.toDoApp.domain.models.Task;
import com.toDoApp.domain.repositories.TaskRepository;
import com.toDoApp.dtos.requests.TaskRequestDTO;
import com.toDoApp.dtos.responses.TaskResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private TaskResponseDTO mapToDTO(Task task) {
        return new TaskResponseDTO(
                task.getId(),
                task.getUserId(),
                task.getTitle(),
                task.getBody(),
                task.getPriority(),
                task.isCompleted(),
                task.getDateCreated(),
                task.getDeadline()
        );
    }

    @Override
    public TaskResponseDTO createTask(TaskRequestDTO requestDTO) {
        Task task = new Task();
        task.setUserId(requestDTO.getUserId());
        task.setTitle(requestDTO.getTitle());
        task.setBody(requestDTO.getBody());
        task.setPriority(requestDTO.getPriority());
        task.setDeadline(requestDTO.getDeadline());
        task.setCompleted(false);

        Task savedTask = taskRepository.save(task);
        return mapToDTO(savedTask);
    }

    @Override
    public Optional<TaskResponseDTO> getTaskById(String taskId) {
        return taskRepository.findById(taskId).map(this::mapToDTO);
    }

    @Override
    public List<TaskResponseDTO> getTasksByUserId(String userId) {
        return taskRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponseDTO> getTasksByCompletionStatus(boolean completed) {
        return taskRepository.findByCompleted(completed)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TaskResponseDTO> updateTask(String taskId, TaskRequestDTO requestDTO) {
        return taskRepository.findById(taskId).map(existingTask -> {
            existingTask.setTitle(requestDTO.getTitle());
            existingTask.setBody(requestDTO.getBody());
            existingTask.setPriority(requestDTO.getPriority());
            existingTask.setDeadline(requestDTO.getDeadline());

            Task updatedTask = taskRepository.save(existingTask);
            return mapToDTO(updatedTask);
        });
    }

    @Override
    public Optional<TaskResponseDTO> markTaskAsCompleted(String taskId) {
        return taskRepository.findById(taskId).map(existingTask -> {
            existingTask.setCompleted(true);

            Task updatedTask = taskRepository.save(existingTask);
            return mapToDTO(updatedTask);
        });
    }

    @Override
    public boolean deleteTask(String taskId) {
        return taskRepository.findById(taskId).map(task -> {
            taskRepository.delete(task);
            return true;
        }).orElse(false);
    }



}
