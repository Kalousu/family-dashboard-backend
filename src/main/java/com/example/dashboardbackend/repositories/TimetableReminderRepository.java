package com.example.dashboardbackend.repositories;

import com.example.dashboardbackend.models.widgets.timetable.TimetableReminder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TimetableReminderRepository extends JpaRepository<TimetableReminder, Long> {
  List<TimetableReminder> findByWidgetItem_Id(Long widgetId);
  Optional<TimetableReminder> findByWidgetItem_IdAndDay(Long widgetId, int day);
}