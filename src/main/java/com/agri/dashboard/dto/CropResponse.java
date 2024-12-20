package com.agri.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CropResponse {

    private String name;
    private String type;
    private LocalDate plantingDate;
    private double expectedYield;
    private double areaCultivated;
    private double waterRequirement;
    private Double unitPrice;

}
