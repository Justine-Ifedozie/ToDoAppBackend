package com.toDoApp.controllers;

import com.toDoApp.dtos.requests.ReminderRequestDTO;
import com.toDoApp.dtos.responses.ReminderResponseDTO;
import com.toDoApp.services.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calendar")
public class CalendarController {

    private final CalendarService calendarService;

    @GetMapping("/user/{userId}/date")
    public ResponseEntity<List<ReminderResponseDTO>> getUserRemindersForACertainDate(
            @PathVariable String userId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(calendarService.getUserRemindersForACertainDate(userId, date));
    }

    @GetMapping("/reminders/{userId}/range")
    public ResponseEntity<List<ReminderResponseDTO>> getRemindersForDateRange(
            @PathVariable String userId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(calendarService.getRemindersForDateRange(userId, startDate, endDate));
    }

    @PostMapping
    public ResponseEntity <ReminderResponseDTO> scheduleReminder (@RequestBody ReminderRequestDTO reminderRequestDTO) {
        return  ResponseEntity.ok(calendarService.scheduleReminder(reminderRequestDTO));
    }

    @PutMapping("/reminder/{reminderId}/reschedule")
    public ResponseEntity <ReminderResponseDTO> rescheduleReminder (@PathVariable String reminderId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newTime) {
        return ResponseEntity.ok(calendarService.rescheduleReminder(reminderId, newTime));
    }

    @DeleteMapping("/reminder/task/{taskId}")
    public ResponseEntity <Void> deleteRemindersForTask (@PathVariable String taskId, @RequestParam LocalDate newTime) {
        calendarService.deleteRemindersForTask(taskId);
        return ResponseEntity.noContent().build();
    }

}
