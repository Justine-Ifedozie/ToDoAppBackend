package com.toDoApp.domain.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "reminders")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Reminder {
    @Id
    private String id;
    private String taskId;
    private String userId;
    private String message;
    private LocalDateTime reminderTime;
    private boolean sent;
}
