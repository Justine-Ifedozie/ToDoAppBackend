package com.toDoApp.domain.repositories;

import com.toDoApp.domain.models.Reminder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class ReminderRepositoryTest {

    @Autowired
    private ReminderRepository reminderRepository;

    @BeforeEach
    void setUp() {
        reminderRepository.deleteAll();
    }

    private Reminder createSampleReminder(String taskId, boolean sent) {
        return new Reminder(null, taskId, LocalDateTime.now().plusHours(1), sent);
    }

    @Test
    void testThatRemindersCanBeFoundByTaskId() {
        Reminder reminder = createSampleReminder("task12", false);
        reminderRepository.save(reminder);

        List<Reminder> reminders = reminderRepository.findByTaskId(reminder.getTaskId());

        assertThat(reminders).isNotEmpty();
        assertThat(reminders.get(0).getTaskId()).isEqualTo(reminder.getTaskId());
    }

    @Test
    void testThatRemindersCanBeFoundByTaskIdAndSent() {
        Reminder reminder = createSampleReminder("task12", true);
        reminderRepository.save(reminder);

        List<Reminder> reminders = reminderRepository.findBySent(reminder.isSent());

        assertThat(reminders).isNotEmpty();
        assertThat(reminders.get(0).isSent()).isTrue();
    }
}