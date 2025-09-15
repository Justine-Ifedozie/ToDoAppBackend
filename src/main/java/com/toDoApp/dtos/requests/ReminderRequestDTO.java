package com.toDoApp.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ReminderRequestDTO {
    private String taskId;
    private String userId;
    private String message;
    private LocalDateTime reminderTime;
}
