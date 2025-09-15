package com.toDoApp.controllers;

import com.toDoApp.dtos.requests.ReminderRequestDTO;
import com.toDoApp.dtos.responses.ReminderResponseDTO;
import com.toDoApp.services.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reminders")
@RequiredArgsConstructor

public class ReminderController {

    private final ReminderService reminderService;

    @PostMapping
    public ResponseEntity<ReminderResponseDTO> createReminder(@RequestBody ReminderRequestDTO requestDTO) {
        return ResponseEntity.ok(reminderService.createReminder(requestDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReminderResponseDTO> getReminderById(@PathVariable String id) {
        return reminderService.getReminderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity <List<ReminderResponseDTO>> getRemindersByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(reminderService.getRemindersByUserId(userId));
    }

    @GetMapping("/reminders/{taskId}")
    public ResponseEntity <List<ReminderResponseDTO>> getRemindersByTaskId(@PathVariable String taskId) {
        return ResponseEntity.ok(reminderService.getRemindersByTaskId(taskId));
    }

    @GetMapping("/reminders/{date}")
    public ResponseEntity <List<ReminderResponseDTO>> getRemindersBefore(@PathVariable String date) {
        LocalDateTime parsedTime = LocalDateTime.parse(date);
        return ResponseEntity.ok(reminderService.getRemindersBefore(parsedTime));
    }

    @PutMapping("/reminder/{reminderId}")
    public ResponseEntity<ReminderResponseDTO> updateReminder(@PathVariable String reminderId, @RequestBody ReminderRequestDTO requestDTO) {
        return ResponseEntity.ok(reminderService.updateReminder(reminderId, requestDTO));
    }

    @DeleteMapping("/reminder/{reminderId}")
    public ResponseEntity<Void> deleteReminder(@PathVariable String reminderId) {
        reminderService.deleteReminder(reminderId);
        return ResponseEntity.noContent().build();
    }



}
