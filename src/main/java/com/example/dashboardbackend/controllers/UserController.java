package com.example.dashboardbackend.controllers;

import com.example.dashboardbackend.dtos.ChangeUserRoleRequest;
import com.example.dashboardbackend.dtos.UserResponse;
import com.example.dashboardbackend.models.User;
import com.example.dashboardbackend.models.enums.UserRole;
import com.example.dashboardbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(Authentication authentication) {
        return new ResponseEntity<>(userService.getUserByName(authentication.getName()), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(
            @PathVariable Long userId,
            Authentication authentication
    ) {
        userService.deleteUser(userId, authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{userId}/role")
    public ResponseEntity<Object> changeUserRole(
            @PathVariable Long userId,
            @RequestBody ChangeUserRoleRequest changeUserRoleRequest,
            Authentication authentication
    ) {
        userService.changeUserRole(userId, changeUserRoleRequest, authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
