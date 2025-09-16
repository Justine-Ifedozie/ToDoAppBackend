package com.toDoApp.services;

import com.toDoApp.domain.models.User;
import com.toDoApp.domain.repositories.UserRepository;
import com.toDoApp.dtos.requests.UserRequestDTO;
import com.toDoApp.dtos.responses.UserResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        requestDTO = new UserRequestDTO("Justine12", "justine@gmail.com", "password123");
        user = new User("1", "Justine12", "justine@gmail.com", "password123");
    }

    @Test
    void testCreateUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO result = userService.createUser(requestDTO);

        assertThat(result.getUsername()).isEqualTo("Justine12");
        assertThat(result.getEmail()).isEqualTo("justine@gmail.com");
        verify(userRepository, times(1)).save(any(User.class));
    }



    @Test
    void testLogin_Success() {
        when(userRepository.findByEmail("justine@gmail.com")).thenReturn(Optional.of(user));

        UserResponseDTO result = userService.login(requestDTO);

        assertThat(result.getEmail()).isEqualTo("justine@gmail.com");
    }

    @Test
    void testLogin_InvalidPassword() {
        when(userRepository.findByEmail("justine@gmail.com")).thenReturn(Optional.of(user));

        UserRequestDTO wrongPasswordDTO = new UserRequestDTO("Justine12", "justine@gmail.com", "wrongPassword");

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.login(wrongPasswordDTO));

        assertEquals("Invalid password", exception.getMessage());
    }

    @Test
    void testLogin_UserNotFound() {
        when(userRepository.findByEmail("unknown@gmail.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                userService.login(requestDTO));
    }


}
