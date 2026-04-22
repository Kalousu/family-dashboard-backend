package com.example.dashboardbackend.models.widgets.timetable;

import com.example.dashboardbackend.models.widgets.WidgetItem;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimetableWatchedUser {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "widget_item_id")
  private WidgetItem widgetItem;

  @Column(nullable = false)
  private Long userId;
}