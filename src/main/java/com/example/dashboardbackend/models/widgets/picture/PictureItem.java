package com.example.dashboardbackend.models.widgets.picture;

import com.example.dashboardbackend.models.widgets.general.WidgetItem;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PictureItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "widget_item_id")
  private WidgetItem widgetItem;

  private String imageUrl;
}