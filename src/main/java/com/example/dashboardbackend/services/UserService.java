package com.example.dashboardbackend.services;

import com.example.dashboardbackend.dtos.user.ChangeUserRoleRequest;
import com.example.dashboardbackend.dtos.user.UpdateUserProfileRequest;
import com.example.dashboardbackend.dtos.user.SetUserPinRequest;
import com.example.dashboardbackend.dtos.user.UserResponse;
import com.example.dashboardbackend.exceptions.UnauthorizedException;
import com.example.dashboardbackend.models.User;
import com.example.dashboardbackend.models.enums.UserAvatarType;
import com.example.dashboardbackend.repositories.UserRepository;
import com.example.dashboardbackend.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Client s3Client;
    private final JwtUtils jwtUtils;

    @Value("${cloudflare.r2.bucket}")
    private String bucket;

    @Value("${cloudflare.r2.public-url}")
    private String publicUrl;

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

        // SYSADMIN can change any user's role
        if (admin.role() != com.example.dashboardbackend.models.enums.UserRole.SYSTEM_ADMIN) {
            // For non-SYSADMIN users, check if they are in the same family
            if (admin.familyId() == null || !admin.familyId().equals(userToPatch.getFamily().getId())) {
                throw new UnauthorizedException("The User you want to update does not exist in your family");
            }
        }

        userToPatch.setUserRole(request.userRole());
        userRepository.save(userToPatch);
    }

    public String updateUserProfile(Long id, UpdateUserProfileRequest request, Authentication authentication) {
        User currentUser = userRepository.findByName(authentication.getName())
            .orElseThrow(() -> new UsernameNotFoundException("Current user not found"));
        User userToUpdate = userRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("User to update not found"));

        // Users can only update their own profile, unless they are SYSADMIN
        if (!currentUser.getId().equals(userToUpdate.getId()) && 
            currentUser.getUserRole() != com.example.dashboardbackend.models.enums.UserRole.SYSTEM_ADMIN) {
            throw new UnauthorizedException("You can only update your own profile");
        }

        // Check if name is being changed
        boolean nameChanged = !userToUpdate.getName().equals(request.name());

        userToUpdate.setName(request.name());
        userToUpdate.setColor(request.color());
        userToUpdate.setAvatar(request.avatar());
        userToUpdate.setAvatarType(request.avatarType());
        
        userRepository.save(userToUpdate);

        // Generate new JWT token if name was changed and it's the current user
        if (nameChanged && currentUser.getId().equals(userToUpdate.getId())) {
            return jwtUtils.generateUserToken(userToUpdate);
        }
        
        return null;
    }

    public void updateUserProfileWithAvatar(Long id, String name, String color, MultipartFile avatar, Authentication authentication) {
        User currentUser = userRepository.findByName(authentication.getName())
            .orElseThrow(() -> new UsernameNotFoundException("Current user not found"));
        User userToUpdate = userRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("User to update not found"));

        // Users can only update their own profile, unless they are SYSADMIN
        if (!currentUser.getId().equals(userToUpdate.getId()) && 
            currentUser.getUserRole() != com.example.dashboardbackend.models.enums.UserRole.SYSTEM_ADMIN) {
            throw new UnauthorizedException("You can only update your own profile");
        }

        // Upload avatar to Cloudflare R2
        String avatarUrl = uploadUserAvatar(userToUpdate.getId(), avatar);

        userToUpdate.setName(name);
        userToUpdate.setColor(color);
        userToUpdate.setAvatar(avatarUrl);
        userToUpdate.setAvatarType(UserAvatarType.URL);
        
        userRepository.save(userToUpdate);
    }

    private String uploadUserAvatar(Long userId, MultipartFile file) {
        String fileExtension = getFileExtension(file.getOriginalFilename());
        String key = "users/" + userId + "/avatar/" + UUID.randomUUID() + fileExtension;

        try {
            s3Client.putObject(
                PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(file.getContentType())
                    .build(),
                RequestBody.fromBytes(file.getBytes())
            );
        } catch (Exception e) {
            throw new RuntimeException("Avatar upload failed", e);
        }

        return publicUrl.endsWith("/") ? publicUrl + key : publicUrl + "/" + key;
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return ".jpg";
        }
        return filename.substring(filename.lastIndexOf("."));
    }

    public void setUserPin(Long id, SetUserPinRequest request, Authentication authentication) {
        User currentUser = userRepository.findByName(authentication.getName())
            .orElseThrow(() -> new UsernameNotFoundException("Current user not found"));
        User userToUpdate = userRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("User to update not found"));

        // Users can only update their own PIN, unless they are SYSADMIN or FAMILY_ADMIN
        if (!currentUser.getId().equals(userToUpdate.getId()) &&
            currentUser.getUserRole() != com.example.dashboardbackend.models.enums.UserRole.SYSTEM_ADMIN &&
            currentUser.getUserRole() != com.example.dashboardbackend.models.enums.UserRole.FAMILY_ADMIN) {
            throw new UnauthorizedException("You can only update your own PIN");
        }

        // Encode the PIN before saving
        String encodedPin = passwordEncoder.encode(request.pin());
        userToUpdate.setPin(encodedPin);
        
        userRepository.save(userToUpdate);
    }
}
