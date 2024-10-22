package com.example.CloudCalculator.service;

import com.example.CloudCalculator.EventStore;
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


    private final String DATASET = "http://assessment-service:8080/v1/dataset";
    private final String RESULT = "http://assessment-service:8081/v1/result";
    private final RestTemplate restTemplate = new RestTemplate();
    private final EventStore eventStore = new EventStore();


    public List<Event> fetchEvents() {
        ResponseEntity<EventResponse> response = restTemplate.exchange(
                DATASET, HttpMethod.GET, null, EventResponse.class);


        List<Event> events = response.getBody().getEvents();

        events.forEach(eventStore::addEvent);

        return events;
    }

    public List<Result> calculateUsage() {
        Map<String, Long> totalUsage = new HashMap<>();

        for (String customerId : eventStore.getAllEvents().keySet()) {
            long usage = eventStore.calculateTotalRuntime(customerId);
            totalUsage.put(customerId, usage);
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

    public ResponseEntity<String> submitResults(Map<String, List<Result>> requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, List<Result>>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(RESULT, request, String.class);

        return restTemplate.postForEntity(RESULT, response, String.class);
    }

}
