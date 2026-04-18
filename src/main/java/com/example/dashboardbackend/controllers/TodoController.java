package com.example.dashboardbackend.controllers;

import com.example.dashboardbackend.dtos.todo.TodoCreateRequest;
import com.example.dashboardbackend.dtos.todo.TodoItemResponse;
import com.example.dashboardbackend.dtos.todo.TodoUpdatePositionRequest;
import com.example.dashboardbackend.dtos.todo.TodoUpdateTextRequest;
import com.example.dashboardbackend.services.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/widgets/todo")
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;

    @GetMapping("/{widgetId}")
    public ResponseEntity<List<TodoItemResponse>> getTodos(
            @PathVariable Long widgetId
    ) {
        return new ResponseEntity<>(todoService.getTodosByWidgetId(widgetId), HttpStatus.OK);
    }

    @PostMapping("/{widgetId}")
    public ResponseEntity<TodoItemResponse> createTodo(
        @PathVariable Long widgetId,
        @RequestBody TodoCreateRequest request
    ) {
        return new ResponseEntity<>(todoService.createTodo(widgetId, request), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/text")
    public ResponseEntity<TodoItemResponse> updateText(
        @PathVariable Long id,
        @RequestBody TodoUpdateTextRequest request
    ) {
        return new ResponseEntity<>(todoService.updateText(id, request), HttpStatus.OK);
    }

    @PatchMapping("/positions")
    public ResponseEntity<Void> updatePositions(
        @RequestBody List<TodoUpdatePositionRequest> requests
    ) {
        todoService.updatePositions(requests);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
