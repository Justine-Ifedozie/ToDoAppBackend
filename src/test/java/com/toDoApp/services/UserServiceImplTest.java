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

import java.util.List;
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
    void testGetUserByEmail() {
        when(userRepository.findByEmail("justine@gmail.com")).thenReturn(Optional.of(user));

        Optional<UserResponseDTO> result = userService.getUserByEmail("justine@gmail.com");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("justine@gmail.com");
    }

    @Test
    void testGetUserByUsername() {
        when(userRepository.findByUsername("Justine12")).thenReturn(Optional.of(user));

        Optional<UserResponseDTO> result = userService.getUserByUsername("Justine12");

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("Justine12");
    }


    @Test
    void testLogin_Success() {
        when(userRepository.findByEmail("justine@gmail.com")).thenReturn(Optional.of(user));

        UserResponseDTO result = userService.login("justine@gmail.com", "password123");

        assertThat(result.getEmail()).isEqualTo("justine@gmail.com");
    }

    @Test
    void testLogin_InvalidPassword() {
        when(userRepository.findByEmail("justine@gmail.com")).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () ->
                userService.login("justine@gmail.com", "wrongPassword"));
    }

    @Test
    void testLogin_UserNotFound() {
        when(userRepository.findByEmail("unknown@gmail.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                userService.login("unknown@gmail.com", "password123"));
    }

    @Test
    void testGetAllUsers() {
        User anotherUser = new User("2", "Esther12", "esther12@gmail.com", "securepass");
        when(userRepository.findAll()).thenReturn(List.of(user, anotherUser));

        List<UserResponseDTO> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("Justine12", result.get(0).getUsername());
        assertEquals("Esther12", result.get(1).getUsername());
    }

}
