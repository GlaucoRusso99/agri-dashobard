package com.agri.dashboard.scheduler;

import com.agri.dashboard.service.ProductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ProductionScheduler {

    @Autowired
    private ProductionService productionService;

    @Scheduled(fixedRate = 3000000)
    public void scheduleProductionUpdates() {
        productionService.generateProductionData();
    }

}
