package com.toDoApp.services;

import com.toDoApp.dtos.requests.ReminderRequestDTO;
import com.toDoApp.dtos.responses.ReminderResponseDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReminderService {
    ReminderResponseDTO createReminder(ReminderRequestDTO requestDTO);
    Optional<ReminderResponseDTO> getReminderById(String reminderId);
    List<ReminderResponseDTO> getRemindersByUserId(String userId);
    List<ReminderResponseDTO> getRemindersByTaskId(String taskId);
    List<ReminderResponseDTO> getRemindersBefore(LocalDateTime dateTime);
    ReminderResponseDTO updateReminder(String reminderId, ReminderRequestDTO requestDTO);
    void deleteReminder(String reminderId);
}
