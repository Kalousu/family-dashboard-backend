package com.example.dashboardbackend.controllers;

import com.example.dashboardbackend.dtos.timetable.*;
import com.example.dashboardbackend.services.TimetableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/widgets/timetable")
@RequiredArgsConstructor
public class TimetableController {

  private final TimetableService timetableService;

  @GetMapping("/{widgetId}")
  public ResponseEntity<TimetableResponse> getTimetable(@PathVariable Long widgetId) {
    return new ResponseEntity<>(timetableService.getTimetable(widgetId), HttpStatus.OK);
  }

  @PostMapping("/{widgetId}/events")
  public ResponseEntity<TimetableEventResponse> createEvent(
      @PathVariable Long widgetId,
      @RequestBody CreateTimetableEventRequest request
  ) {
    return new ResponseEntity<>(timetableService.createEvent(widgetId, request), HttpStatus.CREATED);
  }

  @DeleteMapping("/{widgetId}/events/{eventId}")
  public ResponseEntity<Void> deleteEvent(@PathVariable Long widgetId, @PathVariable Long eventId) {
    timetableService.deleteEvent(eventId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PostMapping("/{widgetId}/reminders")
  public ResponseEntity<TimetableReminderResponse> upsertReminder(
      @PathVariable Long widgetId,
      @RequestBody CreateTimetableReminderRequest request
  ) {
    return new ResponseEntity<>(timetableService.upsertReminder(widgetId, request), HttpStatus.OK);
  }

  @DeleteMapping("/{widgetId}/reminders/{reminderId}")
  public ResponseEntity<Void> deleteReminder(@PathVariable Long widgetId, @PathVariable Long reminderId) {
    timetableService.deleteReminder(reminderId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PutMapping("/{widgetId}/watched-users")
  public ResponseEntity<Void> updateWatchedUsers(
      @PathVariable Long widgetId,
      @RequestBody UpdateWatchedUsersRequest request
  ) {
    timetableService.updateWatchedUsers(widgetId, request);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}