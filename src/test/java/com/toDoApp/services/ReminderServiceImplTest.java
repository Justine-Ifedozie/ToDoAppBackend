package com.toDoApp.services;

import com.toDoApp.domain.models.Reminder;
import com.toDoApp.domain.repositories.ReminderRepository;
import com.toDoApp.dtos.requests.ReminderRequestDTO;
import com.toDoApp.dtos.responses.ReminderResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReminderServiceImplTest {

    @Mock
    private ReminderRepository reminderRepository;

    @InjectMocks
    private ReminderServiceImpl reminderService;

    private Reminder reminder;
    private ReminderRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        requestDTO = new ReminderRequestDTO("user1", "task1", "Do homework", LocalDateTime.now().plusHours(1));
        reminder = new Reminder("1", "task1", "user1", "Do homework", requestDTO.getReminderTime(), false);
    }

    @Test
    void testCreateReminder() {
        when(reminderRepository.save(any(Reminder.class))).thenReturn(reminder);

        ReminderResponseDTO result = reminderService.createReminder(requestDTO);

        assertThat(result.getTaskId()).isEqualTo("task1");
        assertThat(result.getMessage()).isEqualTo("Do homework");
        verify(reminderRepository, times(1)).save(any(Reminder.class));
    }

    @Test
    void testGetReminderById() {
        when(reminderRepository.findById("1")).thenReturn(Optional.of(reminder));

        Optional<ReminderResponseDTO> result = reminderService.getReminderById("1");

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo("1");
    }

    @Test
    void testGetRemindersByUserId() {
        when(reminderRepository.findByUserId("user1")).thenReturn(List.of(reminder));

        List<ReminderResponseDTO> result = reminderService.getRemindersByUserId("user1");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserId()).isEqualTo("user1");
    }

    @Test
    void testGetRemindersByTaskId() {
        when(reminderRepository.findByTaskId("task1")).thenReturn(List.of(reminder));

        List<ReminderResponseDTO> result = reminderService.getRemindersByTaskId("task1");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTaskId()).isEqualTo("task1");
    }

    @Test
    void testGetRemindersBefore() {
        Reminder pastReminder = new Reminder("2", "task2", "user1", "Old Task", LocalDateTime.now().minusDays(1), false);
        when(reminderRepository.findAll()).thenReturn(Arrays.asList(reminder, pastReminder));

        List<ReminderResponseDTO> result = reminderService.getRemindersBefore(LocalDateTime.now());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMessage()).isEqualTo("Old Task");
    }

    @Test
    void testUpdateReminder() {
        when(reminderRepository.findById("1")).thenReturn(Optional.of(reminder));
        when(reminderRepository.save(any(Reminder.class))).thenReturn(reminder);

        ReminderResponseDTO result = reminderService.updateReminder("1", requestDTO);

        assertThat(result.getMessage()).isEqualTo("Do homework");
        verify(reminderRepository, times(1)).save(reminder);
    }

    @Test
    void testUpdateReminder_NotFound() {
        when(reminderRepository.findById("999")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> reminderService.updateReminder("999", requestDTO));
    }

    @Test
    void testDeleteReminder() {
        doNothing().when(reminderRepository).deleteById("1");

        reminderService.deleteReminder("1");

        verify(reminderRepository, times(1)).deleteById("1");
    }
}
