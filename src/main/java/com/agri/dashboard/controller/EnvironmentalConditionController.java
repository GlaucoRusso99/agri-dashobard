package com.agri.dashboard.controller;

import com.agri.dashboard.entity.EnvironmentalCondition;
import com.agri.dashboard.service.EnvironmentalConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/environmental-conditions")
public class EnvironmentalConditionController {

    @Autowired
    private EnvironmentalConditionService environmentalConditionService;

    @GetMapping("/latest")
    public ResponseEntity<EnvironmentalCondition> getLatestCondition() {
        try {
            EnvironmentalCondition latestCondition = environmentalConditionService.getCurrentEnvironmentalCondition();
            return ResponseEntity.ok(latestCondition);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
