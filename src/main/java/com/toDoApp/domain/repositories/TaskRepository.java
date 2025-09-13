package com.toDoApp.domain.repositories;

import com.toDoApp.domain.models.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends MongoRepository<Task,String> {
    List<Task> findByUserId(String userId);
    List<Task> findByCompleted(boolean completed);
}
