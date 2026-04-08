package com.example.dashboardbackend.models;

import com.example.dashboardbackend.models.widgets.WidgetItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Dashboard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "family_id")
    private Family family;

    @OneToMany(mappedBy = "dashboard", cascade = CascadeType.ALL)
    List<WidgetItem> widgetItems;

}
