package com.example.dashboardbackend.repositories;

import com.example.dashboardbackend.models.widgets.calendar.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {
  List<CalendarEvent> findByWidgetItem_Id(Long widgetId);
  int countByWidgetItem_IdAndDate(Long widgetId, LocalDate date);
  
  @Modifying
  @Transactional
  void deleteByWidgetItem_Id(Long widgetId);
}
