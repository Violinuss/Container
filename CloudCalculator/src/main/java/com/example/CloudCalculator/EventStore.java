package com.example.CloudCalculator;

import com.example.CloudCalculator.model.Event;

import java.util.*;

public class EventStore {
    private final Map<String, List<Event>> eventStore;

    public EventStore() {
        this.eventStore = new HashMap<>();
    }

    public void addEvent(Event event) {
        eventStore.computeIfAbsent(event.getCustomerId(), k -> new ArrayList<>()).add(event);
    }

    public List<Event> getEvents(String customerId) {
        return eventStore.getOrDefault(customerId, Collections.emptyList());
    }

    public Map<String, List<Event>> getAllEvents() {
        return eventStore;
    }

    public long calculateTotalRuntime(String customerId) {
        List<Event> events = eventStore.get(customerId);
        long totalRuntime = 0;
        long startTime = -1;

        if (events != null) {
            for (Event event : events) {
                if ("start".equals(event.getEventType())) {
                    startTime = event.getTimestamp();
                } else if ("stop".equals(event.getEventType()) && startTime != -1) {
                    totalRuntime += (event.getTimestamp() - startTime);
                    startTime = -1;
                }
            }
        }
        return totalRuntime;
    }
}
