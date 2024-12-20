package com.agri.dashboard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CropRequest {

    @NotBlank(message = "The crop name is required.")
    private String name;
    @NotBlank(message = "The crop type is required.")
    private String type;
    @NotNull(message = "The planting date is required.")
    private LocalDate plantingDate;
    @Positive(message = "The expected yield must be greater than zero.")
    private Double expectedYield;
    @Positive(message = "The cultivated area must be greater than zero.")
    private Double areaCultivated;
    @NotNull(message = "The waterRequirement is required.")
    private Double waterRequirement;
    @NotNull(message = "The unitPrice is required.")
    private Double unitPrice;

}
