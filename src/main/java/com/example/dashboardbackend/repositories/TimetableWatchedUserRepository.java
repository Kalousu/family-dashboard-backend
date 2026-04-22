package com.example.dashboardbackend.repositories;

import com.example.dashboardbackend.models.widgets.timetable.TimetableWatchedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TimetableWatchedUserRepository extends JpaRepository<TimetableWatchedUser, Long> {
  List<TimetableWatchedUser> findByWidgetItem_Id(Long widgetId);
  void deleteByWidgetItem_Id(Long widgetId);
}