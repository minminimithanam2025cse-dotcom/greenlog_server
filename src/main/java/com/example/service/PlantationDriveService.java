package com.example.service;

import com.example.model.PlantationDrive;

import java.util.List;
import java.util.Map;

public interface PlantationDriveService {
    PlantationDrive createDrive(PlantationDrive drive);
    PlantationDrive getDriveById(Long id);
    List<PlantationDrive> getAllDrives();
    PlantationDrive updateDrive(Long id, PlantationDrive drive);
    void deleteDrive(Long id);
    double getSurvivalRate(Long driveId);
    Map<String, Object> getDriveStatistics(Long driveId);
}
