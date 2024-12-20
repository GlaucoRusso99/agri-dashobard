package com.agri.dashboard.service;

import com.agri.dashboard.entity.EnvironmentalCondition;
import com.agri.dashboard.repository.EnvironmentalConditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Random;

@Service
public class EnvironmentalConditionService {

    @Autowired
    private EnvironmentalConditionRepository environmentalConditionRepository;

    private final Random random = new Random();

    public void generateGlobalCondition() {
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();

        double temperature = generateTemperatureByMonth(month);
        double humidity = generateHumidityByMonth(month);
        double precipitation = generatePrecipitationByMonth(month);

        EnvironmentalCondition condition = new EnvironmentalCondition();
        condition.setDate(today);
        condition.setTemperature(temperature);
        condition.setHumidity(humidity);
        condition.setPrecipitation(precipitation);

        try {
            environmentalConditionRepository.save(condition);
        } catch (Exception e) {
            throw new RuntimeException("Error saving Environmental Condition", e);
        }
    }

    private double generateTemperatureByMonth(int month) {
        if (month >= 3 && month <= 5) { // Spring - 15-20 °C
            return 15 + random.nextDouble() * 5;
        } else if (month >= 6 && month <= 8) { // Summer - 25-30 °C
            return 25 + random.nextDouble() * 5;
        } else if (month >= 9 && month <= 11) { // Autumn - 10-15 °C
            return 10 + random.nextDouble() * 5;
        } else { // Winter - 0-5 °C
            return 0 + random.nextDouble() * 5;
        }
    }

    private double generateHumidityByMonth(int month) {
        if (month >= 6 && month <= 8) { // Summer - 30-40%
            return 30 + random.nextDouble() * 10;
        } else { // Other seasons - 60-70%
            return 60 + random.nextDouble() * 10;
        }
    }

    private double generatePrecipitationByMonth(int month) {
        if (month >= 4 && month <= 9) { // Spring and Summer - 10-20 mm
            return 10 + random.nextDouble() * 10;
        } else { // Autumn and Winter - 30-40 mm
            return 30 + random.nextDouble() * 10;
        }
    }

    public EnvironmentalCondition getCurrentEnvironmentalCondition() {
        return environmentalConditionRepository
                .findTopByOrderByIdDesc()
                .orElseThrow(() -> new RuntimeException("No environmental data available."));
    }

}
