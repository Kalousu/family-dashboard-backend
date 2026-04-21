package com.example.dashboardbackend.config;

import com.example.dashboardbackend.models.Dashboard;
import com.example.dashboardbackend.models.Family;
import com.example.dashboardbackend.models.User;
import com.example.dashboardbackend.models.enums.UserAvatarType;
import com.example.dashboardbackend.models.enums.UserRole;
import com.example.dashboardbackend.models.widgets.general.WidgetConfig;
import com.example.dashboardbackend.models.widgets.general.WidgetItem;
import com.example.dashboardbackend.models.widgets.general.WidgetPosition;
import com.example.dashboardbackend.repositories.DashboardRepository;
import com.example.dashboardbackend.repositories.FamilyRepository;
import com.example.dashboardbackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, DashboardRepository dashboardRepository, FamilyRepository familyRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                Family family1 = new Family();
                family1.setFamilyName("Family1");
                family1.setPassword(passwordEncoder.encode("123"));

                User sysUser = new User();
                sysUser.setAvatar("https://pub-3450ee438ead45c7bddcf1803d1fc37f.r2.dev/images/06cdc993-f91b-41c4-83d0-977ce9dc1304-dekter.jpeg");
                sysUser.setName("Admin");
                sysUser.setPassword(passwordEncoder.encode("333"));
                sysUser.setUserRole(UserRole.SYSTEM_ADMIN);
                sysUser.setAvatarType(UserAvatarType.URL);

                User user1 = new User();
                user1.setName("User1");
                user1.setPassword(passwordEncoder.encode("123"));
                user1.setEmail("user1@example.com");
                user1.setFamily(family1);
                user1.setUserRole(UserRole.USER);

                Dashboard dashboard1 = new Dashboard();

                WidgetItem weatherWidget = new WidgetItem();
                weatherWidget.setType("weather");
                weatherWidget.setCreatedAt(LocalDateTime.now());
                weatherWidget.setDashboard(dashboard1);

                Map<String, Object> weatherSettings = new HashMap<>();
                weatherSettings.put("city", "Mannheim");
                weatherSettings.put("latitude", 49.4891);
                weatherSettings.put("longitude", 8.46694);
                weatherSettings.put("timezone", "Europe/Berlin");

                WidgetPosition weatherWidgetPosition = new WidgetPosition("0", "0", "2", "2");
                weatherWidget.setWidgetPosition(weatherWidgetPosition);

                weatherWidget.setWidgetConfig(new WidgetConfig(
                        "Wetter App",
                        "blue",
                        weatherSettings
                ));

                dashboard1.setWidgetItems(List.of(weatherWidget));

                Family savedFamily = familyRepository.save(family1);

                user1.setFamily(savedFamily);
                userRepository.save(user1);
                userRepository.save(sysUser);

                dashboard1.setFamily(savedFamily);
                dashboardRepository.save(dashboard1);
            }
        };
    }
}
