package com.toDoApp.services;

import com.toDoApp.domain.models.User;
import com.toDoApp.domain.repositories.UserRepository;
import com.toDoApp.dtos.requests.UserRequestDTO;
import com.toDoApp.dtos.responses.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private UserResponseDTO mapToDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }

    private User mapToEntity(UserRequestDTO dto) {
        return new User(
                null,
                dto.getUsername(),
                dto.getEmail(),
                dto.getPassword()
        );
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO requestDTO) {
        User user = mapToEntity(requestDTO);
        User saved = userRepository.save(user);
        return mapToDTO(saved);
    }

    @Override
    public Optional<UserResponseDTO> getUserById(String id) {
        return userRepository.findById(id).map(this::mapToDTO);
    }


    @Override
    public UserResponseDTO login(UserRequestDTO requestDTO) {
        User user = userRepository.findByEmail(requestDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email"));

        if (!user.getPassword().equals(requestDTO.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return mapToDTO(user);
    }

    @Override
    public void deleteUser(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        userRepository.deleteById(userId);
    }


    @Override
    public UserResponseDTO updateUser(String userId, UserRequestDTO requestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        user.setUsername(requestDTO.getUsername());
        user.setEmail(requestDTO.getEmail());
        user.setPassword(requestDTO.getPassword());

        User updated = userRepository.save(user);
        return mapToDTO(updated);
    }
}

