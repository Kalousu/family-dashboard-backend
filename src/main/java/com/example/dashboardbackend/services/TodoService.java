package com.example.dashboardbackend.services;

import com.example.dashboardbackend.dtos.todo.TodoItemResponse;
import com.example.dashboardbackend.models.widgets.todo.TodoItem;
import com.example.dashboardbackend.repositories.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    @Autowired
    private TodoRepository todoRepository;

    public List<TodoItemResponse> getTodosByWidgetId(Long widgetId) {
        List<TodoItem> items = todoRepository.findTodoItemsByWidgetItem_Id(widgetId)
                .orElseThrow(() -> new RuntimeException("Todo-Items for id " + widgetId + " not found"));

        List<TodoItemResponse> listResponse = items.stream().map(item -> new TodoItemResponse(
                item.getId(),
                item.getText(),
                item.isCompleted(),
                item.getSortOrder()
        )).toList();

        return listResponse;
    }
}
