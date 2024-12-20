package com.agri.dashboard.service;

import com.agri.dashboard.entity.Crop;
import com.agri.dashboard.entity.EnvironmentalCondition;
import com.agri.dashboard.repository.CropRepository;
import com.agri.dashboard.repository.EnvironmentalConditionRepository;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductionService {

    @Value("${crop.costs.Cereale}")
    private double cerealCost;
    @Value("${crop.costs.Ortaggio}")
    private double ortaggioCost;
    @Value("${crop.costs.Frutta}")
    private double fruttaCost;
    @Autowired
    private CropRepository cropRepository;
    @Autowired
    private EnvironmentalConditionRepository conditionRepository;

    public void generateProductionData() {

        List<Crop> crops = cropRepository.findAll();

        Optional<EnvironmentalCondition> optionalCondition = conditionRepository.findTopByOrderByIdDesc();
        EnvironmentalCondition conditionToUse = optionalCondition.orElseGet(this::getDefaultEnvironmentalCondition);

        for (Crop crop : crops) {

            if (optionalCondition.isEmpty()) {
                return;
            }

            double temperatureImpact = calculateTemperatureImpact(conditionToUse.getTemperature());
            double precipitationImpact = calculatePrecipitationImpact(conditionToUse.getPrecipitation());
            double humidityImpact = calculateHumidityImpact(conditionToUse.getHumidity());

            NormalDistribution growthDurationDist = new NormalDistribution(100, 15); // Media 100 giorni, Deviazione Standard 15
            int growthDays = Math.max(90, (int) (growthDurationDist.sample() * temperatureImpact)); // Minimo 90 giorni
            LocalDate harvestDate = crop.getPlantingDate().plusDays(growthDays);

            double meanYield = crop.getExpectedYield() * precipitationImpact; // Precipitazioni influenzano resa
            double stdDevYield = meanYield * 0.1; // Deviazione Standard = 10% della resa attesa
            NormalDistribution yieldDist = new NormalDistribution(meanYield, stdDevYield);
            double actualYield = Math.max(0, yieldDist.sample()); // Resa non negativa

            double yieldRatio = actualYield / crop.getExpectedYield();
            double qualityScore = calculateQualityScore(yieldRatio, temperatureImpact, humidityImpact);
            String quality = determineQuality(qualityScore);

            crop.setGrowthDays(growthDays);
            crop.setHarvestDate(harvestDate);
            crop.setActualYield(actualYield);
            crop.setQuality(quality);

            calculateFinancialPerformance(crop);

            cropRepository.save(crop);

        }
    }

    private String determineQuality(double score) {
        if (score >= 0.8) return "Alta";
        if (score >= 0.5) return "Media";
        return "Bassa";
    }

    private double calculateTemperatureImpact(double temperature) {
        if (temperature < 10 || temperature > 35) {
            return 0.8;
        } else if (temperature >= 20 && temperature <= 30) {
            return 1.1;
        }
        return 1.0;
    }

    private double calculatePrecipitationImpact(double precipitation) {
        if (precipitation < 10) {
            return 0.8;
        } else if (precipitation > 100) {
            return 0.9;
        }
        return 1.0;
    }

    private double calculateHumidityImpact(double humidity) {
        if (humidity < 20 || humidity > 80) {
            return 0.85;
        } else if (humidity >= 40 && humidity <= 60) {
            return 1.1;
        }
        return 1.0;
    }

    private double calculateQualityScore(double yieldRatio, double temperatureImpact, double humidityImpact) {
        double baseScore = yieldRatio * 0.8 + temperatureImpact * 0.1 + humidityImpact * 0.1;
        return Math.min(1.0, Math.max(0.0, baseScore));
    }

    private EnvironmentalCondition getDefaultEnvironmentalCondition() {
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();

        EnvironmentalCondition defaultCondition = new EnvironmentalCondition();
        defaultCondition.setDate(today);
        defaultCondition.setTemperature(generateDefaultTemperature(month));
        defaultCondition.setHumidity(generateDefaultHumidity(month));
        defaultCondition.setPrecipitation(generateDefaultPrecipitation(month));
        return defaultCondition;
    }

    private double generateDefaultTemperature(int month) {
        if (month >= 3 && month <= 5) {
            return 15.0;
        } else if (month >= 6 && month <= 8) {
            return 25.0;
        } else if (month >= 9 && month <= 11) {
            return 10.0;
        } else {
            return 5.0;
        }
    }

    private double generateDefaultHumidity(int month) {
        if (month >= 6 && month <= 8) {
            return 40.0;
        } else {
            return 70.0;
        }
    }

    private double generateDefaultPrecipitation(int month) {
        if (month >= 4 && month <= 9) {
            return 30.0;
        } else {
            return 60.0;
        }
    }

    public void calculateFinancialPerformance(Crop crop) {

        double costPerUnitArea = switch (crop.getType()) {
            case "Cereale" -> cerealCost;
            case "Ortaggio" -> ortaggioCost;
            case "Frutta" -> fruttaCost;
            default -> 500.0;
        };

        double price = crop.getUnitPrice();

        double totalProductionCost = costPerUnitArea * crop.getAreaCultivated(); // Costo totale

        double totalRevenue = crop.getActualYield() * price;

        double financialPerformance = totalRevenue - totalProductionCost;

        crop.setTotalProductionCost(totalProductionCost);
        crop.setFinancialPerformance(financialPerformance);
    }

}

