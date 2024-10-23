package com.example.CloudCalculator.controller;

import com.example.CloudCalculator.model.Event;
import com.example.CloudCalculator.model.Result;
import com.example.CloudCalculator.service.CloudCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1") // Basis-URL für den Controller
public class CloudCalculatorController {

    @Autowired
    private CloudCalculatorService cloudCalculatorService;

    @GetMapping("/dataset")
    public ResponseEntity<String> calculateAndSubmit() {
        List<Event> events = cloudCalculatorService.fetchEvents();

        List<Result> results = cloudCalculatorService.calculateUsage();

        Map<String, List<Result>> requestBody = new HashMap<>();
        requestBody.put("result", results);

        ResponseEntity<String> response = cloudCalculatorService.submitResults(requestBody);

        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping("/result")
    public ResponseEntity<List<Result>> getResult() {
        List<Result> calculatedResults = cloudCalculatorService.calculateUsage();
        return ResponseEntity.ok(calculatedResults);
    }


    @PostMapping("/result")
    public ResponseEntity<String> receiveResults(@RequestBody Map<String, List<Result>> results) {
        List<Result> calculatedResults = cloudCalculatorService.calculateUsage();

        Map<String, List<Result>> requestBody = new HashMap<>();
        requestBody.put("result", calculatedResults);

        ResponseEntity<String> response = cloudCalculatorService.submitResults(requestBody);

        return ResponseEntity.ok(response.getBody());
    }

}