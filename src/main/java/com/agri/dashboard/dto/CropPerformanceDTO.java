package com.agri.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CropPerformanceDTO {

    private String name;
    private LocalDate harvestDate;
    private Integer expectedYield;
    private Integer actualYield;
    private int growthDays;
    private String quality;
    private Integer totalProductionCost;
    private Integer financialPerformance;

}
