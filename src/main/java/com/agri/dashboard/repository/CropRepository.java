package com.agri.dashboard.repository;

import com.agri.dashboard.entity.Crop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CropRepository extends JpaRepository<Crop, Long> {

    Optional<Crop> findByName(String name);
    List<Crop> findByType(String type);
}
