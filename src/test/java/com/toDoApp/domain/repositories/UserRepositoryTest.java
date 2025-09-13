package com.toDoApp.domain.repositories;

import com.toDoApp.domain.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clearDatabaseBeforeEachTest(){
        userRepository.deleteAll();
    }

    @Test
    void testFindByEmail() {
        User user = new User();
        user.setUsername("Justine12");
        user.setEmail("justine@gmail.com");
        user.setPassword("password");

        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByEmail("justine@gmail.com");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("justine@gmail.com");
    }

    @Test
    void testFindByUsername() {
        User user = new User();
        user.setUsername("Justine12");
        user.setEmail("justine@gmail.com");
        user.setPassword("password");

        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByUsername("Justine12");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("Justine12");
    }
}