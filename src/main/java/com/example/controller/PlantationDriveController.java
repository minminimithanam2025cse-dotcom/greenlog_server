package com.example.controller;

import com.example.model.PlantationDrive;
import com.example.service.PlantationDriveService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/drives")
public class PlantationDriveController {

    private final PlantationDriveService plantationDriveService;

    public PlantationDriveController(PlantationDriveService plantationDriveService) {
        this.plantationDriveService = plantationDriveService;
    }

    @PostMapping
    public ResponseEntity<PlantationDrive> createDrive(@Valid @RequestBody PlantationDrive drive) {
        return ResponseEntity.status(201).body(plantationDriveService.createDrive(drive));
    }

    @GetMapping
    public ResponseEntity<List<PlantationDrive>> getAllDrives() {
        return ResponseEntity.ok(plantationDriveService.getAllDrives());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlantationDrive> getDriveById(@PathVariable Long id) {
        return ResponseEntity.ok(plantationDriveService.getDriveById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlantationDrive> updateDrive(@PathVariable Long id,
                                                        @Valid @RequestBody PlantationDrive drive) {
        return ResponseEntity.ok(plantationDriveService.updateDrive(id, drive));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDrive(@PathVariable Long id) {
        plantationDriveService.deleteDrive(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/survival-rate")
    public ResponseEntity<Double> getSurvivalRate(@PathVariable Long id) {
        return ResponseEntity.ok(plantationDriveService.getSurvivalRate(id));
    }

    @GetMapping("/{id}/statistics")
    public ResponseEntity<Map<String, Object>> getDriveStatistics(@PathVariable Long id) {
        return ResponseEntity.ok(plantationDriveService.getDriveStatistics(id));
    }
}
