package com.example.dashboardbackend.repositories;

import com.example.dashboardbackend.models.widgets.timetable.TimetableEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TimetableEventRepository extends JpaRepository<TimetableEvent, Long> {
  List<TimetableEvent> findByWidgetItem_Id(Long widgetId);
}