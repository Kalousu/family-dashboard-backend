package com.example.dashboardbackend.repositories;

import com.example.dashboardbackend.models.widgets.todo.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<TodoItem, Long> {
    Optional<List<TodoItem>> findTodoItemsByWidgetItem_Id(Long widgetId);
}
