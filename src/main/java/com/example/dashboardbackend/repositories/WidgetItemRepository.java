package com.example.dashboardbackend.repositories;

import com.example.dashboardbackend.models.widgets.WidgetItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WidgetItemRepository extends JpaRepository<WidgetItem, Long> {
}
