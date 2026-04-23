package com.example.dashboardbackend.controllers;

import com.example.dashboardbackend.dtos.user.ChangeUserRoleRequest;
import com.example.dashboardbackend.dtos.user.UpdateUserProfileRequest;
import com.example.dashboardbackend.dtos.user.SetUserPinRequest;
import com.example.dashboardbackend.dtos.user.UserResponse;
import com.example.dashboardbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

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

    @PatchMapping("/{userId}/profile")
    public ResponseEntity<Object> updateUserProfile(
            @PathVariable Long userId,
            @RequestBody UpdateUserProfileRequest updateUserProfileRequest,
            Authentication authentication,
            HttpServletResponse response
    ) {
        String newToken = userService.updateUserProfile(userId, updateUserProfileRequest, authentication);
        
        // Set new JWT token as cookie if name was changed
        if (newToken != null) {
            Cookie authCookie = new Cookie("auth_token", newToken);
            authCookie.setHttpOnly(true);
            authCookie.setSecure(false); // Set to true in production with HTTPS
            authCookie.setPath("/");
            authCookie.setMaxAge(24 * 60 * 60); // 24 hours
            response.addCookie(authCookie);
        }
        
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{userId}/profile/avatar")
    public ResponseEntity<Object> updateUserProfileWithAvatar(
            @PathVariable Long userId,
            @RequestParam String name,
            @RequestParam String color,
            @RequestParam MultipartFile avatar,
            Authentication authentication
    ) {
        userService.updateUserProfileWithAvatar(userId, name, color, avatar, authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{userId}/pin")
    public ResponseEntity<Object> setUserPin(
            @PathVariable Long userId,
            @RequestBody SetUserPinRequest setUserPinRequest,
            Authentication authentication
    ) {
        userService.setUserPin(userId, setUserPinRequest, authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
