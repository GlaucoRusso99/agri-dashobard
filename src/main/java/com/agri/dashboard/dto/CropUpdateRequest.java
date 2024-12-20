package com.agri.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CropUpdateRequest {

    private String type;
    private LocalDate plantingDate;
    private Double expectedYield;
    private Double areaCultivated;
    private Double waterRequirement;
    private Double unitPrice;

}
