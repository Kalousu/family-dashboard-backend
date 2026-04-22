package com.example.dashboardbackend.controllers;

import com.example.dashboardbackend.dtos.calendar.*;
import com.example.dashboardbackend.services.CalendarEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/widgets/calendar")
@RequiredArgsConstructor
public class CalendarEventController {

  private final CalendarEventService calendarEventService;

  @GetMapping("/{widgetId}")
  public ResponseEntity<List<CalendarEventResponse>> getEvents(@PathVariable Long widgetId) {
    return new ResponseEntity<>(calendarEventService.getEvents(widgetId), HttpStatus.OK);
  }

  @PostMapping("/{widgetId}")
  public ResponseEntity<CalendarEventResponse> createEvent(
      @PathVariable Long widgetId,
      @RequestBody CreateCalendarEventRequest request
  ) {
    return new ResponseEntity<>(calendarEventService.createEvent(widgetId, request), HttpStatus.CREATED);
  }

  @PutMapping("/{eventId}")
  public ResponseEntity<CalendarEventResponse> updateEvent(
      @PathVariable Long eventId,
      @RequestBody UpdateCalendarEventRequest request
  ) {
    return new ResponseEntity<>(calendarEventService.updateEvent(eventId, request), HttpStatus.OK);
  }

  @DeleteMapping("/{eventId}")
  public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
    calendarEventService.deleteEvent(eventId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}