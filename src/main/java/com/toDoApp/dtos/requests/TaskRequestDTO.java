package com.toDoApp.dtos.requests;

import com.toDoApp.domain.models.Priority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class TaskRequestDTO {
    private String userId;
    private String title;
    private String body;
    private Priority priority;
    private LocalDateTime deadline;
}
