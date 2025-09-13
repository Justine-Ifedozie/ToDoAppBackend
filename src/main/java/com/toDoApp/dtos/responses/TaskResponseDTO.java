package com.toDoApp.dtos.responses;

import com.toDoApp.domain.models.Priority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class TaskResponseDTO {
    private String id;
    private String userId;
    private String title;
    private String body;
    private Priority priority;
    private boolean completed;
    private LocalDateTime dateCreated;
    private LocalDateTime deadline;

}
