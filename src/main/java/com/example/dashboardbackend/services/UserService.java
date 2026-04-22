package com.example.dashboardbackend.services;

import com.example.dashboardbackend.dtos.user.ChangeUserRoleRequest;
import com.example.dashboardbackend.dtos.user.UserResponse;
import com.example.dashboardbackend.exceptions.UnauthorizedException;
import com.example.dashboardbackend.models.User;
import com.example.dashboardbackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream().map(user ->
            new UserResponse(
                    user.getId(),
                    user.getName(),
                    user.getUserRole(),
                    user.getFamily() != null ? user.getFamily().getId() : null,
                    user.getAvatar(),
                    user.getAvatarType(),
                    user.getColor(),
                    user.getUserColorMode(),
                    user.getPin() != null && !user.getPin().isEmpty()
            )
        ).toList();
    }

    public UserResponse getUserByName(String name) {
        User user = userRepository.findByName(name).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getUserRole(),
                user.getFamily() != null ? user.getFamily().getId() : null,
                user.getAvatar(),
                user.getAvatarType(),
                user.getColor(),
                user.getUserColorMode(),
                user.getPin() != null && !user.getPin().isEmpty()
        );
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getUserRole(),
            user.getFamily() != null ? user.getFamily().getId() : null,
            user.getAvatar(),
            user.getAvatarType(),
            user.getColor(),
            user.getUserColorMode(),
            user.getPin() != null && !user.getPin().isEmpty()
        );
    }

    public void deleteUser(Long id, Authentication authentication) {
        UserResponse admin = getUserByName(authentication.getName());
        User userToDelete = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User you want to delete not found"));

        if(admin.familyId().equals(userToDelete.getFamily().getId())) {
            throw new UnauthorizedException("The User you want to delete does not exist in your family");
        }

        userRepository.delete(userToDelete);
    }

    public void changeUserRole(Long id, ChangeUserRoleRequest request, Authentication authentication) {
        UserResponse admin = getUserByName(authentication.getName());
        User userToPatch = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User you want to update not found"));

        if(admin.familyId().equals(userToPatch.getFamily().getId())) {
            throw new UnauthorizedException("The User you want to update does not exist in your family");
        }

        userToPatch.setUserRole(request.userRole());
        userRepository.save(userToPatch);
    }
}
