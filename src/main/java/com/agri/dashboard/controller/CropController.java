package com.agri.dashboard.controller;

import com.agri.dashboard.dto.CropPerformanceDTO;
import com.agri.dashboard.dto.CropRequest;
import com.agri.dashboard.dto.CropResponse;
import com.agri.dashboard.dto.CropUpdateRequest;
import com.agri.dashboard.entity.Crop;
import com.agri.dashboard.service.CropService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class CropController {

    @Autowired
    private CropService cropService;

    @GetMapping("crops/new")
    public String showAddCropForm(Model model) {
        model.addAttribute("cropRequest", new CropRequest());
        return "add-crop";
    }

    @PostMapping("addCrop")
    public String addCrop(@ModelAttribute CropRequest cropRequest){
        cropService.addCrop(cropRequest);
        return "redirect:/crops";
    }

    @GetMapping("crops")
    public String getAllCrops(Model model) {
        List<CropResponse> crops = cropService.getAllCrops();
        model.addAttribute("crops", crops);
        return "crops";
    }

    @GetMapping("/crops/delete/{name}")
    public String getCropsByName(@PathVariable String name) {
        cropService.deleteCropByName(name);
        return "redirect:/crops";
    }

    @GetMapping("/crops/update/{name}")
    public String showUpdateCropForm(@PathVariable String name, Model model) {
        CropResponse crop = cropService.getCropsByName(name);
        model.addAttribute("crop", crop);
        return "update-crop";
    }

    @PostMapping("/crops/edit/{name}")
    public String updateCropByName(
            @PathVariable String name,
            @ModelAttribute CropUpdateRequest cropRequest) {
        cropService.updateCropByName(name, cropRequest);
        return "redirect:/crops";
    }

    @GetMapping("/performance/{name}")
    public String showPerformance(@PathVariable("name") String name, Model model) {
        CropPerformanceDTO performance = cropService.getCropPerformanceMetricsByName(name);

        model.addAttribute("performance", performance);

        return "performance-dashboard";
    }

}
