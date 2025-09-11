package com.toDoApp.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ReminderResponseDTO {
    public String id;
    public String taskId;
    public LocalDateTime reminderTime;
    private boolean sent;
}
