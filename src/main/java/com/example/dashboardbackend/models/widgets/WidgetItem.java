package com.example.dashboardbackend.models.widgets;

import com.example.dashboardbackend.models.Dashboard;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
public class WidgetItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne  // ← FEHLT!
    @JoinColumn(name = "dashboard_id")  // ← FEHLT!
    private Dashboard dashboard;  // ← FEHLT!

    @Column(nullable = false)
    private String type;  // ← FEHLT! ("weather", "todo", etc.)

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private WidgetConfig widgetConfig;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private WidgetPosition widgetPosition;

    private LocalDateTime createdAt;
}
