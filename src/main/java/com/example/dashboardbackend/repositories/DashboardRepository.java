package com.example.dashboardbackend.repositories;

import com.example.dashboardbackend.models.Dashboard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DashboardRepository extends JpaRepository<Dashboard, Long> {
    Optional<Dashboard> findByFamily_Id(Long familyId);
}
