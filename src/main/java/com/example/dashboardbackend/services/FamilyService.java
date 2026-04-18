package com.example.dashboardbackend.services;

import com.example.dashboardbackend.dtos.CreateFamilyRequest;
import com.example.dashboardbackend.dtos.UserProfile;
import com.example.dashboardbackend.dtos.UserSelectPageResponse;
import com.example.dashboardbackend.dtos.UserSelectResponse;
import com.example.dashboardbackend.dtos.dashboard.DashboardResponse;
import com.example.dashboardbackend.dtos.dashboard.WidgetResponse;
import com.example.dashboardbackend.exceptions.FamilyAlreadyExistsException;
import com.example.dashboardbackend.models.Dashboard;
import com.example.dashboardbackend.models.Family;
import com.example.dashboardbackend.models.User;
import com.example.dashboardbackend.repositories.DashboardRepository;
import com.example.dashboardbackend.repositories.FamilyRepository;
import com.example.dashboardbackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
                users.stream().map(u -> new UserProfile(
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
                .orElseThrow(() -> new RuntimeException("Family you want to delete not found"));

        familyRepository.delete(familyToDelete);
    }
}
