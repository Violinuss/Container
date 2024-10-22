package com.example.CloudCalculator.controller;

import com.example.CloudCalculator.model.Event;
import com.example.CloudCalculator.model.Result;
import com.example.CloudCalculator.service.CloudCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CloudCalculatorController {

    @Autowired
    private CloudCalculatorService cloudCalculatorService;

    @GetMapping("/api/v1/calculate")
    public String calculateAndSubmit() {
        List<Event> events = cloudCalculatorService.fetchEvents();

        List<Result> results = cloudCalculatorService.calculateUsage(events);

        cloudCalculatorService.submitResults(results);

        return "Berechnung abgeschlossen und Ergebnisse gesendet!";
    }
}

