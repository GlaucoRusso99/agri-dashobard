package com.agri.dashboard.service;


import com.agri.dashboard.dto.CropPerformanceDTO;
import com.agri.dashboard.dto.CropRequest;
import com.agri.dashboard.dto.CropResponse;
import com.agri.dashboard.dto.CropUpdateRequest;
import com.agri.dashboard.entity.Crop;
import com.agri.dashboard.repository.CropRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CropService {

    private final CropRepository cropRepository;
    @Autowired
    public CropService(CropRepository cropRepository) {
        this.cropRepository = cropRepository;
    }

    public void addCrop(CropRequest cropRequest) {
        log.info("Adding crop: {}", cropRequest);
        String normalizedName = cropRequest.getName().substring(0, 1).toUpperCase();

        if (cropRepository.findByName(normalizedName).isPresent()) {
            log.warn("Crop with name '{}' already exists", normalizedName);
            throw new IllegalArgumentException("A crop with the name '" + normalizedName + "' already exists.");
        }

        Crop crop = new Crop();
        crop.setName(normalizedName);
        crop.setType(cropRequest.getType());
        crop.setPlantingDate(cropRequest.getPlantingDate());
        crop.setExpectedYield(cropRequest.getExpectedYield());
        crop.setAreaCultivated(cropRequest.getAreaCultivated());
        crop.setUnitPrice(cropRequest.getUnitPrice());

        try {
            cropRepository.save(crop);
            log.info("Crop '{}' successfully added", normalizedName);
        } catch (Exception e) {
            log.error("Error saving crop '{}'", normalizedName);
            throw new RuntimeException("Error saving crop: {}", e);
        }
    }

    public List<CropResponse> getAllCrops() {
        log.info("Fetching all crops");
        try {
            List<Crop> crops = cropRepository.findAll();
            log.info("Found {} crops", crops.size());
            return crops.stream()
                    .map(this::convertToCropResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("An error occurred while retrieving crops");
            throw new RuntimeException("An error occurred while retrieving crops.", e);
        }
    }

    private CropResponse convertToCropResponse(Crop crop) {
        CropResponse cropResponse = new CropResponse();
        cropResponse.setName(crop.getName());
        cropResponse.setType(crop.getType());
        cropResponse.setPlantingDate(crop.getPlantingDate());
        cropResponse.setExpectedYield(crop.getExpectedYield());
        cropResponse.setAreaCultivated(crop.getAreaCultivated());
        cropResponse.setUnitPrice(crop.getUnitPrice());
        return cropResponse;
    }


    public void deleteCropByName(String name) {
        log.info("Deleting crop with name '{}'", name);
        try {
            Crop crop = cropRepository.findByName(name).orElseThrow(() ->
                    new RuntimeException("Crop with name '" + name + "' not found."));
            cropRepository.delete(crop);
            log.info("Crop '{}' successfully deleted", name);
        } catch (Exception e) {
            log.error("An error occurred while deleting the crop with name '{}'", name);
            throw new RuntimeException("An error occurred while deleting the crop with name: " + name, e);
        }
    }

    public CropResponse getCropsByName(String name) {
        log.info("Fetching crop with name '{}'", name);
        try {
            Crop crop = cropRepository.findByName(name)
                    .orElseThrow(() ->
                            new RuntimeException("Crop with name '" + name + "' not found."));
            log.info("Crop '{}' found", name);
            return convertToCropResponse(crop);
        } catch (Exception e) {
            log.error("An error occurred while retrieving the crop with name '{}'", name);
            throw new RuntimeException("An error occurred while retrieving the crop with name: " + name, e);
        }
    }

    public void updateCropByName(String name, CropUpdateRequest cropRequest) {
        log.info("Updating crop with name '{}'", name);
        Crop crop = cropRepository.findByName(name)
                .orElseThrow(() -> {
                    log.warn("Crop with name '{}' not found", name);
                    return new RuntimeException("Crop with name '" + name + "' not found.");
                });


        if (cropRequest.getPlantingDate() != null) {
            crop.setPlantingDate(cropRequest.getPlantingDate());
        }
        if (cropRequest.getExpectedYield() != null) {
            crop.setExpectedYield(cropRequest.getExpectedYield());
        }
        if (cropRequest.getType() != null) {
            crop.setType(cropRequest.getType());
        }
        if (cropRequest.getAreaCultivated() != null) {
            crop.setAreaCultivated(cropRequest.getAreaCultivated());
        }
        if (cropRequest.getUnitPrice() != null) {
            crop.setUnitPrice(cropRequest.getUnitPrice());
        }

        try {
            cropRepository.save(crop);
            log.info("Crop '{}' successfully updated", name);
        } catch (Exception e) {
            log.error("Error updating crop '{}': {}", name, e.getMessage(), e);
            throw new RuntimeException("Error during crop update: {}", e);
        }
    }

    public CropPerformanceDTO getCropPerformanceMetricsByName(String name) {

        Crop crop = cropRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Crop not found for name: " + name));

        CropPerformanceDTO dto = new CropPerformanceDTO();
        dto.setName(crop.getName());
        dto.setHarvestDate(crop.getHarvestDate());
        dto.setActualYield(crop.getActualYield().intValue());
        dto.setExpectedYield(crop.getExpectedYield().intValue());
        dto.setGrowthDays(crop.getGrowthDays());
        dto.setQuality(crop.getQuality());
        dto.setTotalProductionCost(crop.getTotalProductionCost().intValue());
        dto.setFinancialPerformance(crop.getFinancialPerformance().intValue());

        return dto;
    }


}
