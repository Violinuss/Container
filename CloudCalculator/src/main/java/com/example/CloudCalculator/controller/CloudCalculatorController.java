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

    @GetMapping("/result")
    public ResponseEntity<Map<String, List<Result>>> getResult() {
        List<Event> events = cloudCalculatorService.fetchEvents();

        List<Result> calculatedResults = cloudCalculatorService.calculateUsage();

        if (calculatedResults.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Map<String, List<Result>> responseBody = new HashMap<>();
        responseBody.put("result", calculatedResults);

        return ResponseEntity.ok(responseBody);
    }

}