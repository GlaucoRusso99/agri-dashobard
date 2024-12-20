package com.agri.dashboard.scheduler;

import com.agri.dashboard.entity.EnvironmentalCondition;
import com.agri.dashboard.repository.EnvironmentalConditionRepository;
import com.agri.dashboard.service.EnvironmentalConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class EnvironmentalConditionScheduler {
    @Autowired
    private EnvironmentalConditionService environmentalConditionGenerator;

    @Scheduled(fixedRate = 600000)
    public void generateEnvironmentalConditions() {
        environmentalConditionGenerator.generateGlobalCondition();
    }

}
