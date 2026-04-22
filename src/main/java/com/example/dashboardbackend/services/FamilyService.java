package com.example.dashboardbackend.services;

import com.example.dashboardbackend.dtos.dashboard.DashboardResponse;
import com.example.dashboardbackend.dtos.dashboard.PermissionResponse;
import com.example.dashboardbackend.dtos.dashboard.WidgetResponse;
import com.example.dashboardbackend.dtos.family.CreateFamilyRequest;
import com.example.dashboardbackend.dtos.family.CreateFamilyResponse;
import com.example.dashboardbackend.dtos.family.FamilyResponse;
import com.example.dashboardbackend.dtos.family.UpdateFamilyNameRequest;
import com.example.dashboardbackend.dtos.user.UserProfileResponse;
import com.example.dashboardbackend.dtos.user.UserSelectPageResponse;
import com.example.dashboardbackend.exceptions.FamilyAlreadyExistsException;
import com.example.dashboardbackend.exceptions.FamilyNotFoundException;
import com.example.dashboardbackend.models.Dashboard;
import com.example.dashboardbackend.models.Family;
import com.example.dashboardbackend.models.User;
import com.example.dashboardbackend.models.enums.UserRole;
import com.example.dashboardbackend.repositories.DashboardRepository;
import com.example.dashboardbackend.repositories.FamilyRepository;
import com.example.dashboardbackend.repositories.UserRepository;
import com.example.dashboardbackend.security.principals.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FamilyService {
    private final FamilyRepository familyRepository;
    private final DashboardRepository dashboardRepository;
    private final WidgetService widgetService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DashboardResponse getDashboardByFamilyId(Long familyId, Authentication authentication) {
        Dashboard dashboard = dashboardRepository.findByFamily_Id(familyId)
                .orElseGet(() -> {
                    // Create a new dashboard if none exists for this family
                    Family family = familyRepository.findById(familyId)
                            .orElseThrow(() -> new FamilyNotFoundException("Family with id " + familyId + " not found"));
                    
                    Dashboard newDashboard = new Dashboard();
                    newDashboard.setFamily(family);
                    return dashboardRepository.save(newDashboard);
                });

        List<WidgetResponse> widgetResponseList = dashboard.getWidgetItems() != null 
                ? dashboard.getWidgetItems().stream().map(widgetItem -> new WidgetResponse(
                        widgetItem.getId(),
                        widgetItem.getType(),
                        widgetItem.getWidgetConfig(),
                        widgetItem.getWidgetPosition(),
                        widgetItem.getCreatedAt(),
                        widgetService.getWidgetData(widgetItem.getId(), widgetItem.getType(), widgetItem.getWidgetConfig())
                )).toList()
                : List.of();

        // Determine permissions based on user role
        PermissionResponse permissions = getPermissionsForUser(authentication);

        return new DashboardResponse(
                dashboard.getId(),
                dashboard.getFamily().getId(),
                widgetResponseList,
                permissions
        );
    }

    private PermissionResponse getPermissionsForUser(Authentication authentication) {
        System.out.println("DEBUG: Authentication object: " + authentication);
        if (authentication != null) {
            System.out.println("DEBUG: Authentication name: " + authentication.getName());
            System.out.println("DEBUG: Principal type: " + authentication.getPrincipal().getClass().getSimpleName());
            System.out.println("DEBUG: Principal: " + authentication.getPrincipal());
        }
        
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal userPrincipal) {
            User user = userPrincipal.getUser();
            UserRole role = user.getUserRole();
            
            System.out.println("DEBUG: User " + user.getName() + " has role: " + role);
            
            // FAMILY_ADMIN has full permissions, USER has read-only
            boolean isAdmin = role == UserRole.FAMILY_ADMIN || role == UserRole.SYSTEM_ADMIN;
            
            System.out.println("DEBUG: isAdmin = " + isAdmin);
            
            return new PermissionResponse(
                    isAdmin, // canEditLayout
                    isAdmin, // canAddWidgets  
                    isAdmin, // canDeleteWidgets
                    true     // canEditWidgetData - both roles can edit widget content
            );
        }
        
        System.out.println("DEBUG: No authentication or wrong principal type");
        // Default to read-only if no authentication
        return new PermissionResponse(false, false, false, true);
    }

    public UserSelectPageResponse getUsersForFamily(Long familyId) {
        List<User> users = userRepository.findByFamilyId(familyId);
        return new UserSelectPageResponse(
                users.stream().map(u -> new UserProfileResponse(
                        u.getId(),
                        u.getName(),
                        u.getAvatar(),
                        u.getAvatarType(),
                        u.getUserRole(),
                        u.getPin() != null && !u.getPin().isEmpty(),
                        u.getColor()
                )).toList()
        );
    }

    public CreateFamilyResponse createFamily(CreateFamilyRequest createFamilyRequest) {
        var family = new Family();
        family.setFamilyName(createFamilyRequest.familyName());
        family.setPassword(passwordEncoder.encode(createFamilyRequest.password()));
        family.setEmail(createFamilyRequest.email());

        try{
            Family savedFamily = familyRepository.save(family);
            return new CreateFamilyResponse(savedFamily.getId(), savedFamily.getFamilyName());
        }catch(DataIntegrityViolationException e){
            throw new FamilyAlreadyExistsException(createFamilyRequest.familyName());
        }
    }

    public void deleteFamilyById(Long familyId) {
        Family familyToDelete = familyRepository.findById(familyId)
                .orElseThrow(() -> new FamilyNotFoundException("Family you want to delete not found"));

        familyRepository.delete(familyToDelete);
    }

    public List<FamilyResponse> getFamilies() {
        List<Family> families = familyRepository.findAll();
        return families.stream().map(family -> new FamilyResponse(
                family.getId(),
                family.getFamilyName(),
                family.getEmail()
        )).toList();
    }

    public FamilyResponse getFamilyById(Long familyId) {
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new FamilyNotFoundException("Family with Id " + familyId + " not found"));

        return new FamilyResponse(
                family.getId(),
                family.getFamilyName(),
                family.getEmail()
        );
    }

    public void updateFamilyName(Long familyId, UpdateFamilyNameRequest updateFamilyNameRequest) {
        Family familyToUpdate = familyRepository.findById(familyId)
                .orElseThrow(() -> new FamilyNotFoundException("Family with Id " + familyId + " not found"));

        familyToUpdate.setFamilyName(updateFamilyNameRequest.familyName());
        try{
            familyRepository.save(familyToUpdate);
        }catch(DataIntegrityViolationException e){
            throw new FamilyAlreadyExistsException(updateFamilyNameRequest.familyName());
        }
    }
}
