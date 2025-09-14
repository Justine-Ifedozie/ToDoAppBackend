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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUserCanBeRegistered() {
        UserRequestDTO request = new UserRequestDTO("Justine12", "justine@gmail.com", "password123");

        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword123");
        when(userRepository.findByEmail("justine@gmail.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("Justine12")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        UserResponseDTO response = userService.createUser(request);

        assertThat(response.getUsername()).isEqualTo("Justine12");
        assertThat(response.getEmail()).isEqualTo("justine@gmail.com");
    }

    @Test
    void registerExistingUserWithSameEmailThrowsException() {
        UserRequestDTO request = new UserRequestDTO("Justine12", "justine@gmail", "password123");
        when(userRepository.findByEmail("justine@gmail")).thenReturn(Optional.of(new User()));

        assertThatThrownBy(() -> userService.createUser(request)).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("Email already taken");
    }

    @Test
    void registerExistingUserWithSameUsernameThrowsException() {
        UserRequestDTO request = new UserRequestDTO("Justine12", "justine@gmail", "password123");
        when(userRepository.findByEmail("justine@gmail")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("Justine12")).thenReturn(Optional.of(new User()));

        assertThatThrownBy(() -> userService.createUser(request)).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("Username already taken");
    }

    @Test
    void testUserCanLogin() {
        User existingUser = new User("1", "Justine12", "justine@gmail.com", "hashedPassword");
        when(userRepository.findByEmail("justine@gmail.com")).thenReturn(Optional.of(existingUser));

        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);

        UserResponseDTO response = userService.login("justine@gmail.com", "password123");

        assertThat(response.getUsername()).isEqualTo("Justine12");
        assertThat(response.getEmail()).isEqualTo(existingUser.getEmail());
    }

    @Test
    void testLoginUserWithWrongPasswordThrowsException() {
        User user = new User("1", "Justine12", "justine@gmail.com", "hashedPassword");
        when(userRepository.findByEmail("justine@gmail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(false);

        assertThatThrownBy(() -> userService.login("justine@gmail.com", "wrongPassword")).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("Wrong password");
    }

    @Test
    void testLoginWithWrongEmailThrowsException() {
        when(userRepository.findByEmail("unknown@gmail.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.login("unknown@gmail.com", "wrongPassword")).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("Invalid email");
    }

    @Test
    void testGetUserByUsernameWorks(){
        String username = "Justine12";
        User user = new User("1", username, "justine@gmail.com", "hashedPassword");
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Optional<UserResponseDTO> result = userService.getUserByUsername(username);

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo(username);
        assertThat(result.get().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void testGetUserByEmailWorks(){
        String email = "justine@gmail.com";
        User user = new User("1", "Justine12", email, "hashedPassword");
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<UserResponseDTO> result = userService.getUserByEmail(email);

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo(user.getUsername());
        assertThat(result.get().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void testGetUserByEmailNotFound() {
        when(userRepository.findByEmail("missing@gmail.com")).thenReturn(Optional.empty());

        Optional<UserResponseDTO> result = userService.getUserByEmail("missing@gmail.com");

        assertThat(result).isNotPresent();
    }

    @Test
    void testGetUserByUsernameNotFound() {
        when(userRepository.findByUsername("missingUser")).thenReturn(Optional.empty());

        Optional<UserResponseDTO> result = userService.getUserByUsername("missingUser");

        assertThat(result).isNotPresent();
    }

}

