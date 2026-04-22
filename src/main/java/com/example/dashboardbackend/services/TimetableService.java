package com.example.dashboardbackend.services;

import com.example.dashboardbackend.dtos.timetable.*;
import com.example.dashboardbackend.models.widgets.timetable.*;
import com.example.dashboardbackend.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimetableService {

  private final TimetableEventRepository eventRepository;
  private final TimetableReminderRepository reminderRepository;
  private final TimetableWatchedUserRepository watchedUserRepository;
  private final WidgetItemRepository widgetItemRepository;

  public TimetableResponse getTimetable(Long widgetId) {
    List<TimetableEventResponse> events = eventRepository.findByWidgetItem_Id(widgetId)
                                                         .stream().map(e -> new TimetableEventResponse(e.getId(), e.getTitle(), e.getSlot(), e.getDay(), e.getUserId()))
                                                         .toList();

    List<TimetableReminderResponse> reminders = reminderRepository.findByWidgetItem_Id(widgetId)
                                                                  .stream().map(r -> new TimetableReminderResponse(r.getId(), r.getDay(), r.getText()))
                                                                  .toList();

    List<Long> watchedUserIds = watchedUserRepository.findByWidgetItem_Id(widgetId)
                                                     .stream().map(TimetableWatchedUser::getUserId)
                                                     .toList();

    return new TimetableResponse(events, reminders, watchedUserIds);
  }

  public TimetableEventResponse createEvent(Long widgetId, CreateTimetableEventRequest request) {
    if (request.title() == null || request.title().isBlank()) throw new RuntimeException("Titel darf nicht leer sein");
    if (request.slot() < 1 || request.slot() > 9) throw new RuntimeException("Slot muss zwischen 1 und 9 liegen");
    if (request.day() < 0 || request.day() > 4) throw new RuntimeException("Tag muss zwischen 0 und 4 liegen");

    var widgetItem = widgetItemRepository.findById(widgetId)
                                         .orElseThrow(() -> new RuntimeException("Widget " + widgetId + " nicht gefunden"));

    if (!widgetItem.getType().equals("timetable")) throw new RuntimeException("Widget " + widgetId + " ist kein Timetable");

    TimetableEvent event = new TimetableEvent();
    event.setWidgetItem(widgetItem);
    event.setTitle(request.title());
    event.setSlot(request.slot());
    event.setDay(request.day());
    event.setUserId(request.userId());

    TimetableEvent saved = eventRepository.save(event);
    return new TimetableEventResponse(saved.getId(), saved.getTitle(), saved.getSlot(), saved.getDay(), saved.getUserId());
  }

  public void deleteEvent(Long eventId) {
    eventRepository.deleteById(eventId);
  }

  public TimetableReminderResponse upsertReminder(Long widgetId, CreateTimetableReminderRequest request) {
    if (request.day() < 0 || request.day() > 4) throw new RuntimeException("Tag muss zwischen 0 und 4 liegen");
    if (request.text() == null || request.text().isBlank()) throw new RuntimeException("Text darf nicht leer sein");

    var widgetItem = widgetItemRepository.findById(widgetId)
                                         .orElseThrow(() -> new RuntimeException("Widget " + widgetId + " nicht gefunden"));

    TimetableReminder reminder = reminderRepository
        .findByWidgetItem_IdAndDay(widgetId, request.day())
        .orElse(new TimetableReminder());

    reminder.setWidgetItem(widgetItem);
    reminder.setDay(request.day());
    reminder.setText(request.text());

    TimetableReminder saved = reminderRepository.save(reminder);
    return new TimetableReminderResponse(saved.getId(), saved.getDay(), saved.getText());
  }

  public void deleteReminder(Long reminderId) {
    reminderRepository.deleteById(reminderId);
  }

  @Transactional
  public void updateWatchedUsers(Long widgetId, UpdateWatchedUsersRequest request) {
    var widgetItem = widgetItemRepository.findById(widgetId)
                                         .orElseThrow(() -> new RuntimeException("Widget " + widgetId + " nicht gefunden"));

    watchedUserRepository.deleteByWidgetItem_Id(widgetId);

    List<TimetableWatchedUser> newWatched = request.userIds().stream().map(userId -> {
      TimetableWatchedUser w = new TimetableWatchedUser();
      w.setWidgetItem(widgetItem);
      w.setUserId(userId);
      return w;
    }).toList();

    watchedUserRepository.saveAll(newWatched);
  }
}