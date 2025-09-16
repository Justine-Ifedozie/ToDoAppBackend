package com.toDoApp.services;

import com.toDoApp.domain.repositories.ReminderRepository;
import com.toDoApp.dtos.requests.ReminderRequestDTO;
import com.toDoApp.dtos.responses.ReminderResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import({ReminderServiceImpl.class, CalendarServiceImpl.class})
class CalendarServiceTest {

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private ReminderRepository reminderRepository;

    @BeforeEach
    void setUp() {
        reminderRepository.deleteAll();
    }

    private ReminderRequestDTO createSampleDTO(String taskId, String userId, LocalDateTime time) {
        return new ReminderRequestDTO(taskId, userId, "Test message", time);
    }

    @Test
    void testScheduleAndFetchReminderForDate() {
        LocalDateTime reminderTime = LocalDate.now().atTime(10, 0);
        calendarService.scheduleReminder(createSampleDTO("task1", "user1", reminderTime));

        List<ReminderResponseDTO> reminders = calendarService.getUserRemindersForACertainDate("user1", LocalDate.now());

        assertThat(reminders).hasSize(1);
        assertThat(reminders.get(0).getTaskId()).isEqualTo("task1");
    }

    @Test
    void testGetUserRemindersForACertainDateRange() {
        LocalDate today = LocalDate.now();
        calendarService.scheduleReminder(createSampleDTO("task1", "user1", today.atTime(10, 0)));
        calendarService.scheduleReminder(createSampleDTO("task2", "user1", today.plusDays(2).atTime(12, 0)));

        List<ReminderResponseDTO> reminders = calendarService.getRemindersForDateRange("user1", today, today.plusDays(3));

        assertThat(reminders).hasSize(2);
    }

    @Test
    void testRescheduleReminder() {
        LocalDateTime oldTime = LocalDate.now().atTime(9, 0);
        ReminderResponseDTO scheduled = calendarService.scheduleReminder(createSampleDTO("task1", "user1", oldTime));

        LocalDateTime newTime = oldTime.plusHours(2);
        ReminderResponseDTO updated = calendarService.rescheduleReminder(scheduled.getId(), newTime);

        assertThat(updated.getReminderTime()).isEqualTo(newTime);
    }

    @Test
    void testDeleteRemindersForTask() {
        calendarService.scheduleReminder(createSampleDTO("task1", "user1", LocalDate.now().atTime(8, 0)));
        calendarService.scheduleReminder(createSampleDTO("task1", "user1", LocalDate.now().atTime(9, 0)));

        calendarService.deleteRemindersForTask("task1");

        assertThat(reminderRepository.findByTaskId("task1")).isEmpty();
    }
}
