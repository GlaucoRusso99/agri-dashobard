package com.agri.dashboard.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Crop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    private LocalDate plantingDate;
    private Double expectedYield;
    private Double areaCultivated;
    private LocalDate harvestDate;
    private Double actualYield;
    private int growthDays;
    private String quality;
    private Double totalProductionCost;
    private Double financialPerformance;
    @Column(nullable = false)
    private Double unitPrice;

}
