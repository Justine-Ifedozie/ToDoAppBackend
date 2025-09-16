package com.toDoApp.services;

import com.toDoApp.domain.models.Reminder;
import com.toDoApp.domain.repositories.ReminderRepository;
import com.toDoApp.dtos.requests.ReminderRequestDTO;
import com.toDoApp.dtos.responses.ReminderResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {

    private final ReminderService reminderService;
    private final ReminderRepository reminderRepository;

    @Override
    public List<ReminderResponseDTO> getUserRemindersForACertainDate(String userId, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        return reminderService.getRemindersByUserId(userId).stream()
                .filter(r -> !r.getReminderTime().isBefore(start) && r.getReminderTime().isBefore(end))
                .collect(Collectors.toList());
    }

    @Override
    public List<ReminderResponseDTO> getRemindersForDateRange(String userId, LocalDate start, LocalDate end) {
        LocalDateTime rangeStart = start.atStartOfDay();
        LocalDateTime rangeEnd = end.plusDays(1).atStartOfDay();

        return reminderService.getRemindersByUserId(userId).stream()
                .filter(r -> !r.getReminderTime().isBefore(rangeStart) && r.getReminderTime().isBefore(rangeEnd))
                .collect(Collectors.toList());
    }

    @Override
    public ReminderResponseDTO scheduleReminder(ReminderRequestDTO requestDTO) {
        return reminderService.createReminder(requestDTO);
    }

    @Override
    public ReminderResponseDTO rescheduleReminder(String reminderId, LocalDateTime newTime) {
        Reminder reminder = reminderRepository.findById(reminderId)
                .orElseThrow(() -> new RuntimeException("Reminder not found"));

        reminder.setReminderTime(newTime);
        reminderRepository.save(reminder);

        return new ReminderResponseDTO(
                reminder.getId(),
                reminder.getTaskId(),
                reminder.getUserId(),
                reminder.getMessage(),
                reminder.getReminderTime(),
                reminder.isSent()
        );
    }

    @Override
    public void deleteRemindersForTask(String taskId) {
        List<Reminder> reminders = reminderRepository.findByTaskId(taskId);
        reminderRepository.deleteAll(reminders);
    }
}
