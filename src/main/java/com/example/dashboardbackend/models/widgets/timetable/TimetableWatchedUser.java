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
public class TimetableWatchedUser {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "widget_item_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private WidgetItem widgetItem;

  @Column(nullable = false)
  private Long userId;
}
