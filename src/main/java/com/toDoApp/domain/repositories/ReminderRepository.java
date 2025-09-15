package com.toDoApp.domain.repositories;

import com.toDoApp.domain.models.Reminder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReminderRepository extends MongoRepository<Reminder,String> {
    List<Reminder> findByTaskId(String taskId);
    List<Reminder> findBySent(boolean sent);
    List<Reminder> findByUserId(String userId);
}
