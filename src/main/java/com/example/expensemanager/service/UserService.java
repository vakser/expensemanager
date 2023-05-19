package com.example.expensemanager.service;

import com.example.expensemanager.dto.UserDto;
import com.example.expensemanager.entity.User;
import com.example.expensemanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public void save(UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user = mapToEntity(userDto);
        user.setUserId(UUID.randomUUID().toString());
        userRepository.save(user);
    }

    private User mapToEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    public User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserEmail = auth.getName();
        return userRepository.findByEmail(loggedInUserEmail).orElseThrow(() -> new UsernameNotFoundException("User not found for the email: " + loggedInUserEmail));
    }
}
