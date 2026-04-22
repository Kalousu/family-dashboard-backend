package com.example.dashboardbackend.models.widgets.calendar;

import com.example.dashboardbackend.models.widgets.WidgetItem;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CalendarEvent {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "widget_item_id")
  private WidgetItem widgetItem;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private LocalDate date;

  @Column(nullable = false)
  private String color;

  @Column(nullable = false)
  private boolean allDay;

  private LocalTime startTime; // null wenn allDay = true
}