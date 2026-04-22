package com.example.dashboardbackend.services;

import com.example.dashboardbackend.dtos.calendar.*;
import com.example.dashboardbackend.models.User;
import com.example.dashboardbackend.models.widgets.WidgetItem;
import com.example.dashboardbackend.models.widgets.calendar.CalendarEvent;
import com.example.dashboardbackend.repositories.CalendarEventRepository;
import com.example.dashboardbackend.repositories.UserRepository;
import com.example.dashboardbackend.repositories.WidgetItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarEventService {

  private final CalendarEventRepository calendarEventRepository;
  private final WidgetItemRepository widgetItemRepository;

  public List<CalendarEventResponse> getEvents(Long widgetId) {
    return calendarEventRepository.findByWidgetItem_Id(widgetId).stream()
                                  .map(this::toResponse)
                                  .toList();
  }

  public CalendarEventResponse createEvent(Long widgetId, CreateCalendarEventRequest request) {


    WidgetItem widgetItem = widgetItemRepository.findById(widgetId)
                                                .orElseThrow(() -> new RuntimeException("Widget " + widgetId + " nicht gefunden"));

    if (!widgetItem.getType().equals("calendar")) {
      throw new RuntimeException("Widget " + widgetId + " ist kein Kalender");
    }

    int count = calendarEventRepository.countByWidgetItem_IdAndDate(widgetId, request.date());
    if (count >= 100) throw new RuntimeException("Maximale Anzahl von 100 Events pro Tag erreicht");
    if (request.allDay() && request.startTime() != null) throw new RuntimeException("startTime muss null sein wenn allDay = true");
    if (request.title() == null || request.title().isBlank()) throw new RuntimeException("Titel darf nicht leer sein");

    CalendarEvent event = new CalendarEvent();
    event.setWidgetItem(widgetItem);
    event.setTitle(request.title());
    event.setDate(request.date());
    event.setColor(request.color());
    event.setAllDay(request.allDay());
    event.setStartTime(request.startTime());

    return toResponse(calendarEventRepository.save(event));
  }

  public CalendarEventResponse updateEvent(Long eventId, UpdateCalendarEventRequest request) {
    CalendarEvent event = calendarEventRepository.findById(eventId)
                                                 .orElseThrow(() -> new RuntimeException("Event " + eventId + " nicht gefunden"));

    if (request.title() == null || request.title().isBlank()) throw new RuntimeException("Titel darf nicht leer sein");
    if (request.allDay() && request.startTime() != null) throw new RuntimeException("startTime muss null sein wenn allDay = true");

    event.setTitle(request.title());
    event.setDate(request.date());
    event.setColor(request.color());
    event.setAllDay(request.allDay());
    event.setStartTime(request.startTime());

    return toResponse(calendarEventRepository.save(event));
  }

  public void deleteEvent(Long eventId) {
    calendarEventRepository.deleteById(eventId);
  }

  private CalendarEventResponse toResponse(CalendarEvent event) {
    return new CalendarEventResponse(
        event.getId(),
        event.getTitle(),
        event.getDate(),
        event.getColor(),
        event.isAllDay(),
        event.getStartTime()
    );
  }
}