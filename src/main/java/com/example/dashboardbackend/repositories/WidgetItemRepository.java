package com.example.dashboardbackend.repositories;

import com.example.dashboardbackend.models.widgets.WidgetItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WidgetItemRepository extends JpaRepository<WidgetItem, Long> {
}
