package com.toDoApp.services;

import com.toDoApp.domain.models.Reminder;
import com.toDoApp.domain.repositories.ReminderRepository;
import com.toDoApp.dtos.requests.ReminderRequestDTO;
import com.toDoApp.dtos.responses.ReminderResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReminderServiceImpl implements ReminderService {

    private final ReminderRepository reminderRepository;

    private ReminderResponseDTO mapToDTO(Reminder reminder) {
        return new ReminderResponseDTO(
                reminder.getId(),
                reminder.getTaskId(),
                reminder.getUserId(),
                reminder.getMessage(),
                reminder.getReminderTime(),
                reminder.isSent()
        );
    }

    private Reminder mapToEntity(ReminderRequestDTO dto) {
        return new Reminder(
                null,
                dto.getTaskId(),
                dto.getUserId(),
                dto.getMessage(),
                dto.getReminderTime(),
                false
        );
    }

    @Override
    public ReminderResponseDTO createReminder(ReminderRequestDTO requestDTO) {
        Reminder reminder = mapToEntity(requestDTO);
        Reminder saved = reminderRepository.save(reminder);
        return mapToDTO(saved);
    }

    @Override
    public Optional<ReminderResponseDTO> getReminderById(String reminderId) {
        return reminderRepository.findById(reminderId)
                .map(this::mapToDTO);
    }

    @Override
    public List<ReminderResponseDTO> getRemindersByUserId(String userId) {
        return reminderRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReminderResponseDTO> getRemindersByTaskId(String taskId) {
        return reminderRepository.findByTaskId(taskId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReminderResponseDTO> getRemindersBefore(LocalDateTime dateTime) {
        return reminderRepository.findAll().stream()
                .filter(r -> r.getReminderTime().isBefore(dateTime))
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ReminderResponseDTO updateReminder(String reminderId, ReminderRequestDTO requestDTO) {
        Reminder reminder = reminderRepository.findById(reminderId)
                .orElseThrow(() -> new RuntimeException("Reminder not found"));

        reminder.setTaskId(requestDTO.getTaskId());
        reminder.setUserId(requestDTO.getUserId());
        reminder.setMessage(requestDTO.getMessage());
        reminder.setReminderTime(requestDTO.getReminderTime());

        Reminder updated = reminderRepository.save(reminder);
        return mapToDTO(updated);
    }

    @Override
    public void deleteReminder(String reminderId) {
        reminderRepository.deleteById(reminderId);
    }
}
