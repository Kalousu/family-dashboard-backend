package com.example.dashboardbackend.services;

import com.example.dashboardbackend.dtos.todo.*;
import com.example.dashboardbackend.models.widgets.todo.TodoItem;
import com.example.dashboardbackend.repositories.TodoRepository;
import com.example.dashboardbackend.repositories.WidgetItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;
    private final WidgetItemRepository widgetItemRepository;

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
    public TodoItemResponse createTodo(Long widgetId, TodoCreateRequest request) {
        int nextPosition = todoRepository.findTopByWidgetItem_IdOrderBySortOrderDesc(widgetId)
                                         .map(item -> item.getSortOrder() + 1)
                                         .orElse(1);

        TodoItem newItem = new TodoItem();
        newItem.setText(request.text());
        newItem.setCompleted(request.completed());
        newItem.setSortOrder(nextPosition);
        newItem.setWidgetItem(widgetItemRepository.findById(widgetId)
                                                  .orElseThrow(() -> new RuntimeException("Widget " + widgetId + " nicht gefunden")));

        TodoItem saved = todoRepository.save(newItem);
        return new TodoItemResponse(saved.getId(), saved.getText(), saved.isCompleted(), saved.getSortOrder());
    }

    public TodoItemResponse updateText(Long id, TodoUpdateTextRequest request) {
        TodoItem item = todoRepository.findById(id)
                                      .orElseThrow(() -> new RuntimeException("Todo " + id + " nicht gefunden"));

        item.setText(request.text());
        TodoItem saved = todoRepository.save(item);
        return new TodoItemResponse(saved.getId(), saved.getText(), saved.isCompleted(), saved.getSortOrder());
    }

    public void updatePositions(List<TodoUpdatePositionRequest> requests) {
        requests.forEach(req -> {
            TodoItem item = todoRepository.findById(req.id())
                                          .orElseThrow(() -> new RuntimeException("Todo " + req.id() + " nicht gefunden"));
            item.setSortOrder(req.sortOrder());
            todoRepository.save(item);
        });
    }

    public void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }

    public TodoItemResponse updateCompleted(Long id, TodoUpdateCompletedRequest request) {
        TodoItem item = todoRepository.findById(id)
                                      .orElseThrow(() -> new RuntimeException("Todo " + id + " nicht gefunden"));

        item.setCompleted(request.completed());
        TodoItem saved = todoRepository.save(item);
        return new TodoItemResponse(saved.getId(), saved.getText(), saved.isCompleted(), saved.getSortOrder());
    }
}
