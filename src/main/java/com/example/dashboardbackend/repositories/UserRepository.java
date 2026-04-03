package com.example.dashboardbackend.repositories;

import com.example.dashboardbackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
