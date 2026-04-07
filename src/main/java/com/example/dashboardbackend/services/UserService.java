package com.example.dashboardbackend.services;

import com.example.dashboardbackend.dtos.UserResponse;
import com.example.dashboardbackend.models.User;
import com.example.dashboardbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<UserResponse> getUsers(){
        List<UserResponse> users = new ArrayList<>();

        userRepository.findAll().stream().forEach(user -> {
            users.add(new UserResponse(
                    user.getId(),
                    user.getName(),
                    user.getUserRole(),
                    user.getFamily(),
                    user.getUserPfp(),
                    user.getPfpColour()
            ));
        });

        return users;
    }
}
