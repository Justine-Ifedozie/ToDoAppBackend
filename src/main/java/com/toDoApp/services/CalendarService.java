package com.toDoApp.services;

import com.toDoApp.dtos.requests.ReminderRequestDTO;
import com.toDoApp.dtos.responses.ReminderResponseDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CalendarService {
    List<ReminderResponseDTO> getRemindersForDate(String userId, LocalDate date);
    List<ReminderResponseDTO> getRemindersForDateRange(String userId, LocalDate start, LocalDate end);
    ReminderResponseDTO scheduleReminder(ReminderRequestDTO requestDTO);
    ReminderResponseDTO rescheduleReminder(String reminderId, LocalDateTime newTime);
    void deleteRemindersForTask(String taskId);
}

