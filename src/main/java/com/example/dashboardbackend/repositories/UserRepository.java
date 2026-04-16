package com.example.dashboardbackend.repositories;

import com.example.dashboardbackend.models.User;
import com.example.dashboardbackend.models.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
    Optional<User> findByNameAndUserRole(String name, UserRole userRole);
    List<User> findByFamilyId(Long familyId);
}
