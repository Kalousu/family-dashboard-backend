package com.example.dashboardbackend.repositories;

import com.example.dashboardbackend.models.Family;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyRepository extends JpaRepository<Family, Long> {
}
