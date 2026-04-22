package com.example.dashboardbackend.models.widgets.timetable;

import com.example.dashboardbackend.models.widgets.WidgetItem;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimetableReminder {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "widget_item_id")
  private WidgetItem widgetItem;

  @Column(nullable = false)
  private int day; // 0-4

  @Column(nullable = false)
  private String text;
}