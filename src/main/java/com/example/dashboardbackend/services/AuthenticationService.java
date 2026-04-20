package com.example.dashboardbackend.services;

import com.example.dashboardbackend.dtos.*;
import com.example.dashboardbackend.dtos.auth.AuthRequest;
import com.example.dashboardbackend.dtos.auth.AuthResponse;
import com.example.dashboardbackend.dtos.auth.RegisterRequest;
import com.example.dashboardbackend.exceptions.FamilyNotFoundException;
import com.example.dashboardbackend.models.Family;
import com.example.dashboardbackend.models.enums.UserAvatarType;
import com.example.dashboardbackend.models.enums.UserRole;
import com.example.dashboardbackend.repositories.FamilyRepository;
import com.example.dashboardbackend.repositories.UserRepository;
import com.example.dashboardbackend.security.jwt.JwtUtils;
import com.example.dashboardbackend.security.principals.FamilyPrincipal;
import com.example.dashboardbackend.security.principals.UserPrincipal;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import com.example.dashboardbackend.models.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
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

        if (avatar != null && !avatar.isEmpty()) {
            String avatarKey = mediaService.uploadFile(avatar);
            avatarURI = "https://pub-3450ee438ead45c7bddcf1803d1fc37f.r2.dev/" + avatarKey;
            avatarType = UserAvatarType.URL;
        } else {
            avatarURI = request.pfpIcon();
            avatarType = UserAvatarType.ICON;
        }

        var user = new User();

        user.setName(request.name());
        user.setPassword(null);
        user.setPin(request.pin() != null ? passwordEncoder.encode(request.pin()) : null);
        user.setUserRole(UserRole.USER);
        user.setFamily(familyRepository.findById(request.familyId())
                .orElseThrow(() -> new FamilyNotFoundException("Family for User " + request.name() + " not found")));
        user.setAvatar(avatarURI);
        user.setAvatarType(avatarType);

        userRepository.save(user);
        String jwtToken = jwtUtils.generateUserToken(user);
        return new AuthResponse(jwtToken);
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.name(), request.password()));
        User user = userRepository.findByName(request.name()).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        String jwtToken = jwtUtils.generateUserToken(user);
        return new AuthResponse(jwtToken);
    }

    public Object login(LoginRequest request, HttpServletResponse response) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.name(), request.password())
        );

        UserDetails principal = (UserDetails) auth.getPrincipal();

        if (principal instanceof UserPrincipal userPrincipal) {
            User user = userPrincipal.getUser();
            if (user.getUserRole() == UserRole.SYSTEM_ADMIN) {
                String token = jwtUtils.generateUserToken(user);
                addCookie(response, "auth_token", token);
                return new SysAdminLoginResponse("SYSADMIN");
            }
            throw new BadCredentialsException("Use profile selection");
        } else if (principal instanceof FamilyPrincipal familyPrincipal) {
            Family family = familyPrincipal.getFamily();
            String token = jwtUtils.generateFamilyToken(family);
            addCookie(response, "family_token", token);
            List<UserProfile> profiles = getProfiles(family.getId());
            return new FamilyLoginResponse(family.getId(), profiles, "FAMILY");
        }

        throw new BadCredentialsException("Invalid credentials");
    }

    public UserSelectResponse selectUser(String familyToken, UserSelectRequest request, HttpServletResponse response) {
        try {
            if (!jwtUtils.isValidFamilyToken(familyToken)) {
                throw new BadCredentialsException("Invalid family token");
            }

            Long familyId = jwtUtils.extractFamilyId(familyToken);
            User user = userRepository.findById(request.userId())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (!user.getFamily().getId().equals(familyId)) {
                throw new BadCredentialsException("User not in family");
            }

            if (user.getPin() != null && !user.getPin().isEmpty()) {
                if (request.pin() == null ||
                        !passwordEncoder.matches(request.pin(), user.getPin())) {
                    throw new BadCredentialsException("Invalid PIN");
                }
            }

            String token = jwtUtils.generateUserToken(user);
            addCookie(response, "auth_token", token);
            return new UserSelectResponse(user.getId(), user.getUserRole());
        } catch (Exception e) {
            System.err.println("Error in selectUser: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private List<UserProfile> getProfiles(Long familyId) {
        return userRepository.findByFamilyId(familyId).stream()
                .map(user -> new UserProfile(
                        user.getId(),
                        user.getName(),
                        user.getAvatar(),
                        user.getAvatarType(),
                        user.getUserRole(),
                        user.getPin() != null && !user.getPin().isEmpty(),
                        user.getColor()
                ))
                .toList();
    }

    private void addCookie(HttpServletResponse response, String key, String value) {
        ResponseCookie cookie = ResponseCookie.from(key, value)
                .httpOnly(true)
                .secure(false)  // false for local development (HTTP)
                .path("/")
                .sameSite("Lax")  // Changed to Lax for same-site requests in development
                .maxAge(Duration.ofDays(7))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public StatusResponse getStatus(String authToken, String familyToken) {
        if(authToken != null){
            Claims claims = jwtUtils.extractAllClaims(authToken);
            String role = claims.get("role", String.class);
            if("SYSTEM_ADMIN".equals(role)){
                return new StatusResponse("SYSADMIN");
            }
            return new StatusResponse("USER");
        }
        if(familyToken != null && jwtUtils.isValidFamilyToken(familyToken)) {
            return new StatusResponse("FAMILY");
        }
        return new StatusResponse("NONE");
    }

    public void logoutUser(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("auth_token", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(0)  // Delete cookie
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
