package com.toDoApp.services;

import com.toDoApp.domain.models.Priority;
import com.toDoApp.domain.models.Task;
import com.toDoApp.domain.repositories.TaskRepository;
import com.toDoApp.dtos.requests.TaskRequestDTO;
import com.toDoApp.dtos.responses.TaskResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task;
    private TaskRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        requestDTO = new TaskRequestDTO("user123", "Test Task", "This is a task body", Priority.HIGH, LocalDateTime.now().plusDays(2));

        task = new Task(
                "task123",
                "user123",
                "Test Task",
                "This is a task body",
                Priority.HIGH,
                false,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2)
        );
    }

    @Test
    void testCreateTaskWorks(){
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponseDTO response = taskService.createTask(requestDTO);

        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("Test Task");
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testGetTaskById_Found() {
        when(taskRepository.findById("task123")).thenReturn(Optional.of(task));

        Optional<TaskResponseDTO> response = taskService.getTaskById("task123");

        assertThat(response).isPresent();
        assertThat(response.get().getId()).isEqualTo("task123");
    }

    @Test
    void testGetTaskById_NotFound() {
        when(taskRepository.findById("invalidId")).thenReturn(Optional.empty());

        Optional<TaskResponseDTO> response = taskService.getTaskById("invalidId");

        assertThat(response).isEmpty();
    }

    @Test
    void testGetTasksByUserId() {
        when(taskRepository.findByUserId("user123")).thenReturn(Arrays.asList(task));

        List<TaskResponseDTO> tasks = taskService.getTasksByUserId("user123");

        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0).getUserId()).isEqualTo("user123");
    }

    @Test
    void testGetTasksByCompletionStatus() {
        when(taskRepository.findByCompleted(false)).thenReturn(Arrays.asList(task));

        List<TaskResponseDTO> tasks = taskService.getTasksByCompletionStatus(false);

        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0).isCompleted()).isFalse();
    }

    @Test
    void testUpdateTaskSuccess() {
        when(taskRepository.findById("task123")).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskRequestDTO updatedRequest = new TaskRequestDTO("user123", "Updated Task", "Updated body", Priority.MEDIUM, LocalDateTime.now().plusDays(5));

        Optional<TaskResponseDTO> response = taskService.updateTask("task123", updatedRequest);

        assertThat(response).isPresent();
        assertThat(response.get().getTitle()).isEqualTo("Updated Task");
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testUpdateTaskNotFound() {
        when(taskRepository.findById("invalidId")).thenReturn(Optional.empty());

        TaskRequestDTO updatedRequest = new TaskRequestDTO("user123", "Updated Task", "Updated body", Priority.LOW, LocalDateTime.now().plusDays(5));
        Optional<TaskResponseDTO> response = taskService.updateTask("invalidId", updatedRequest);

        assertThat(response).isEmpty();
    }

    @Test
    void markTaskAsCompleted_ShouldUpdateCompletionStatus() {
        when(taskRepository.findById("task123")).thenReturn(Optional.of(task));
        task.setCompleted(true);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Optional<TaskResponseDTO> response = taskService.markTaskAsCompleted("task123");

        assertThat(response).isPresent();
        assertThat(response.get().isCompleted()).isTrue();
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void markTaskAsComplete_taskNotFound() {
        when(taskRepository.findById("invalidId")).thenReturn(Optional.empty());

        Optional<TaskResponseDTO> response = taskService.markTaskAsCompleted("invalidId");

        assertThat(response).isEmpty();
    }

    @Test
    void testDeleteTask_Success() {
        when(taskRepository.findById("task123")).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).delete(task);

        boolean result = taskService.deleteTask("task123");

        assertThat(result).isTrue();
        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    void testDeleteTask_NotFound() {
        when(taskRepository.findById("invalidId")).thenReturn(Optional.empty());

        boolean result = taskService.deleteTask("invalidId");

        assertThat(result).isFalse();
        verify(taskRepository, never()).delete(any(Task.class));
    }
}