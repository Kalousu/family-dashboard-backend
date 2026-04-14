package com.example.dashboardbackend.services;

import com.example.dashboardbackend.dtos.auth.AuthRequest;
import com.example.dashboardbackend.dtos.auth.AuthResponse;
import com.example.dashboardbackend.dtos.auth.RegisterRequest;
import com.example.dashboardbackend.models.enums.UserAvatarType;
import com.example.dashboardbackend.models.enums.UserRole;
import com.example.dashboardbackend.repositories.FamilyRepository;
import com.example.dashboardbackend.repositories.UserRepository;
import com.example.dashboardbackend.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.dashboardbackend.models.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor()
public class AuthenticationService {
    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final MediaService mediaService;

    public AuthResponse register(RegisterRequest request, MultipartFile avatar) {
        String avatarURI;
        UserAvatarType avatarType;

        if(avatar != null && !avatar.isEmpty()) {
            String avatarKey = mediaService.uploadFile(avatar);
            avatarURI = "https://pub-3450ee438ead45c7bddcf1803d1fc37f.r2.dev/" + avatarKey;
            avatarType = UserAvatarType.URL;
        } else {
            avatarURI = request.pfpIcon();
            avatarType = UserAvatarType.ICON;
        }

        var user = new User();

        user.setName(request.name());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setUserRole(UserRole.USER);
        user.setFamily(familyRepository.findById(request.familyId()).orElseThrow(() -> new UsernameNotFoundException("User not found")));
        user.setAvatar(avatarURI);
        user.setAvatarType(avatarType);

        userRepository.save(user);
        String jwtToken = jwtUtils.generateToken(user);
        return new AuthResponse(jwtToken);
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.name(), request.password()));
        User user = userRepository.findByName(request.name()).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        String jwtToken = jwtUtils.generateToken(user);
        return new AuthResponse(jwtToken);
    }
}
