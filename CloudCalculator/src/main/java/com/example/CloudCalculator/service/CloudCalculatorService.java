package com.example.CloudCalculator.service;

import com.example.CloudCalculator.model.Event;
import com.example.CloudCalculator.model.EventResponse;
import com.example.CloudCalculator.model.Result;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.util.*;

@Service
public class CloudCalculatorService {

    private final String DATASET = "http://assessment:8080/v1/dataset";
    private final String RESULT = "http://assessment:8080/v1/result";
    private final RestTemplate restTemplate = new RestTemplate();

    public List<Event> fetchEvents() {
        ResponseEntity<EventResponse> response = restTemplate.exchange(
                DATASET, HttpMethod.GET, null, EventResponse.class);
        return response.getBody().getEvents();
    }

    public List<Result> calculateUsage(List<Event> events) {
        Map<String, Map<String, Long>> workloadMap = new HashMap<>();
        Map<String, Long> totalUsage = new HashMap<>();

        events.sort(Comparator.comparingLong(Event::getTimestamp));

        for (Event event : events) {
            String customerId = event.getCustomerId();
            String workloadId = event.getWorkloadId();
            long timestamp = event.getTimestamp();
            String eventType = event.getEventType();

            workloadMap.putIfAbsent(customerId, new HashMap<>());

            if ("start".equals(eventType)) {
                workloadMap.get(customerId).put(workloadId, timestamp);
            } else if ("stop".equals(eventType)) {
                if (workloadMap.get(customerId).containsKey(workloadId)) {
                    long startTime = workloadMap.get(customerId).get(workloadId);
                    long usageTime = timestamp - startTime;
                    totalUsage.put(customerId, totalUsage.getOrDefault(customerId, 0L) + usageTime);
                    workloadMap.get(customerId).remove(workloadId);
                }
            }
        }

        List<Result> results = new ArrayList<>();
        totalUsage.forEach((customerId, usage) -> {
            Result result = new Result();
            result.setCustomerId(customerId);
            result.setConsumption(usage);
            results.add(result);
        });

        return results;
    }

    public void submitResults(List<Result> results) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        Map<String, List<Result>> requestBody = new HashMap<>();
        requestBody.put("result", results);

        HttpEntity<Map<String, List<Result>>> request = new HttpEntity<>(requestBody, headers);
        restTemplate.postForEntity(RESULT, request, String.class);
    }



}
