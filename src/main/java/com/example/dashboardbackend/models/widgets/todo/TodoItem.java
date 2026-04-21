package com.example.dashboardbackend.models.widgets.todo;

import com.example.dashboardbackend.models.widgets.general.WidgetItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TodoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "widget_item_id")
    private WidgetItem widgetItem;

    private String text;
    private boolean completed;
    private int sortOrder;
}
