package com.toDoApp.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Task {

    @Id
    private String id;

    private String userId;

    private String title;
    private String body;
    private Priority priority;

    private boolean completed;
    private LocalDateTime dateCreated;
    private LocalDateTime deadline;
}
