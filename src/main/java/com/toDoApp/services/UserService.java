package com.toDoApp.services;

import com.toDoApp.dtos.requests.UserRequestDTO;
import com.toDoApp.dtos.responses.UserResponseDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO requestDTO);

    Optional<UserResponseDTO> getUserById(String userId);

    UserResponseDTO updateUser(String userId, UserRequestDTO requestDTO);

    void deleteUser(String userId);

    UserResponseDTO login(UserRequestDTO requestDTO);
}
