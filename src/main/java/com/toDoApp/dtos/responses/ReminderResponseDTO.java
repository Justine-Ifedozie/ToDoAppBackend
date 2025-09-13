package com.toDoApp.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ReminderResponseDTO {
    private String id;
    private String taskId;
    private LocalDateTime reminderTime;
    private boolean sent;
}
