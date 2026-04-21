package com.example.dashboardbackend.repositories;

import com.example.dashboardbackend.models.widgets.picture.PictureItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PictureRepository extends JpaRepository<PictureItem, Long> {
  Optional<PictureItem> findByWidgetItem_Id(Long widgetId);
}