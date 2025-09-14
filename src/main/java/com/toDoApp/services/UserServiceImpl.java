package com.toDoApp.services;

import com.toDoApp.domain.models.User;
import com.toDoApp.domain.repositories.UserRepository;
import com.toDoApp.dtos.requests.UserRequestDTO;
import com.toDoApp.dtos.responses.UserResponseDTO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO requestDTO) {
        if (userRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already taken");
        }

        if (userRepository.findByUsername(requestDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already taken");
        }

        String hashedPassword = passwordEncoder.encode(requestDTO.getPassword());
        User user = new User(null, requestDTO.getUsername(), requestDTO.getEmail(), hashedPassword);
        User savedUser = userRepository.save(user);

        return new UserResponseDTO(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }

    @Override
    public UserResponseDTO login(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPassword())){
            throw new IllegalArgumentException("Wrong password");
        }

        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail());
    }

    @Override
    public Optional<UserResponseDTO> getUserByUsername(String username){
        return userRepository.findByUsername(username).map(user -> new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail()));
    }

    @Override
    public Optional<UserResponseDTO> getUserByEmail(String email){
        return userRepository.findByEmail(email).map(user -> new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail()));
    }
}
