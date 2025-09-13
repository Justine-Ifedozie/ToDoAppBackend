package com.toDoApp.domain.repositories;

import com.toDoApp.domain.models.Priority;
import com.toDoApp.domain.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    private Task createSampleTask(String userId,boolean completed) {
        return  new Task(null, userId, "Attend SK class", "Learn python flask and django", Priority.HIGH, completed, LocalDateTime.now(), LocalDateTime.now().plusDays(3));
    }

    @Test
    void testTaskCanBeFoundByUserId() {
        Task task = createSampleTask("Justine12", false);
        taskRepository.save(task);

        List<Task> tasks = taskRepository.findByUserId("Justine12");

        assertThat(tasks).isNotEmpty();
        assertThat(tasks.get(0).getUserId()).isEqualTo("Justine12");
    }

    @Test
    void testFindByCompletionStatus() {
        Task completedTask = createSampleTask("Justine12", true);
        taskRepository.save(completedTask);

        List<Task> tasks = taskRepository.findByCompleted(true);

        assertThat(tasks).isNotEmpty();
        assertThat(tasks.get(0).isCompleted()).isTrue();
    }
}