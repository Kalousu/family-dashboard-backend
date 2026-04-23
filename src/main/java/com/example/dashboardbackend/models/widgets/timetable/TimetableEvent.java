package com.example.dashboardbackend.models.widgets.timetable;

import com.example.dashboardbackend.models.widgets.WidgetItem;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimetableEvent {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "widget_item_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private WidgetItem widgetItem;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private int slot; // 1-9

  @Column(nullable = false)
  private int day; // 0-4

  @Column(nullable = false)
  private Long userId;
}
