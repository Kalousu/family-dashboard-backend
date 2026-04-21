package com.example.dashboardbackend.services;

import com.example.dashboardbackend.dtos.dashboard.DashboardResponse;
import com.example.dashboardbackend.dtos.dashboard.WidgetResponse;
import com.example.dashboardbackend.dtos.family.CreateFamilyRequest;
import com.example.dashboardbackend.dtos.family.FamilyResponse;
import com.example.dashboardbackend.dtos.family.UpdateFamilyNameRequest;
import com.example.dashboardbackend.dtos.user.UserProfileResponse;
import com.example.dashboardbackend.dtos.user.UserSelectPageResponse;
import com.example.dashboardbackend.exceptions.FamilyAlreadyExistsException;
import com.example.dashboardbackend.exceptions.FamilyNotFoundException;
import com.example.dashboardbackend.models.Dashboard;
import com.example.dashboardbackend.models.Family;
import com.example.dashboardbackend.models.User;
import com.example.dashboardbackend.repositories.DashboardRepository;
import com.example.dashboardbackend.repositories.FamilyRepository;
import com.example.dashboardbackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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

    public DashboardResponse getDashboardByFamilyId(Long familyId) {
        Dashboard dashboard = dashboardRepository.findByFamily_Id(familyId).orElseThrow(() -> new RuntimeException("No dashboard found"));

        List<WidgetResponse> widgetResponseList = dashboard.getWidgetItems().stream().map(widgetItem -> new WidgetResponse(
                widgetItem.getId(),
                widgetItem.getType(),
                widgetItem.getWidgetConfig(),
                widgetItem.getWidgetPosition(),
                widgetItem.getCreatedAt(),
                widgetService.getWidgetData(widgetItem.getId(), widgetItem.getType(), widgetItem.getWidgetConfig())
        )).toList();

        return new DashboardResponse(
                dashboard.getId(),
                dashboard.getFamily().getId(),
                widgetResponseList,
                null
        );
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
                        u.getPin() != null && !u.getPin().isEmpty()
                )).toList()
        );
    }

    public void createFamily(CreateFamilyRequest createFamilyRequest) {
        var family = new Family();
        family.setFamilyName(createFamilyRequest.familyName());
        family.setPassword(passwordEncoder.encode(createFamilyRequest.password()));
        family.setEmail(createFamilyRequest.email());

        try{
            familyRepository.save(family);
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
