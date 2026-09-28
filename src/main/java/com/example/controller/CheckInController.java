package com.example.controller;

import com.example.model.CheckIn;
import com.example.service.CheckInService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/checkins")
public class CheckInController {

    private final CheckInService checkInService;

    public CheckInController(CheckInService checkInService) {
        this.checkInService = checkInService;
    }

    @PostMapping
    public ResponseEntity<CheckIn> createCheckIn(@Valid @RequestBody CheckIn checkIn) {
        return ResponseEntity.status(201).body(checkInService.createCheckIn(checkIn));
    }

    @GetMapping
    public ResponseEntity<List<CheckIn>> getAllCheckIns() {
        return ResponseEntity.ok(checkInService.getAllCheckIns());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CheckIn> getCheckInById(@PathVariable Long id) {
        return ResponseEntity.ok(checkInService.getCheckInById(id));
    }

    @GetMapping("/tree/{treeId}")
    public ResponseEntity<List<CheckIn>> getCheckInsByTree(@PathVariable Long treeId) {
        return ResponseEntity.ok(checkInService.getCheckInsByTree(treeId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCheckIn(@PathVariable Long id) {
        checkInService.deleteCheckIn(id);
        return ResponseEntity.noContent().build();
    }
}

