package com.example.dashboardbackend.services;

import com.example.dashboardbackend.dtos.UserResponse;
import com.example.dashboardbackend.models.User;
import com.example.dashboardbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<UserResponse> getUsers() {
        List<UserResponse> users = new ArrayList<>();

        userRepository.findAll().stream().forEach(user -> {
            users.add(new UserResponse(
                    user.getId(),
                    user.getName(),
                    user.getUserRole(),
                    user.getFamily().getId(),
                    user.getAvatar(),
                    user.getAvatarType(),
                    user.getColor(),
                    user.getUserColorMode()
            ));
        });

        return users;
    }

    public UserResponse getUserByName(String name) {
        User user = userRepository.findByName(name).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getUserRole(),
                user.getFamily().getId(),
                user.getAvatar(),
                user.getAvatarType(),
                user.getColor(),
                user.getUserColorMode()
        );
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getUserRole(),
            user.getFamily().getId(),
            user.getAvatar(),
            user.getAvatarType(),
            user.getColor(),
            user.getUserColorMode()
        );
    }
}
