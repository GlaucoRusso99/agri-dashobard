package com.agri.dashboard.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessRequest {

    @NotBlank(message = "The process name is required.")
    private String name;

    @NotBlank(message = "The process description is required.")
    private String description;

}
