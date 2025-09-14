package com.toDoApp.services;

import com.toDoApp.dtos.requests.UserRequestDTO;
import com.toDoApp.dtos.responses.UserResponseDTO;

import java.util.Optional;

public interface UserService {
    UserResponseDTO createUser(UserRequestDTO requestDTO);
    Optional<UserResponseDTO> getUserByEmail(String email);
    Optional<UserResponseDTO> getUserByUsername(String username);

    UserResponseDTO login(String email, String password);
}
