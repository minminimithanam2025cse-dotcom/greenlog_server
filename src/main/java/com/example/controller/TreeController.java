package com.example.controller;

import com.example.model.Tree;
import com.example.service.TreeService;
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
@RequestMapping("/api/trees")
public class TreeController {

    private final TreeService treeService;

    public TreeController(TreeService treeService) {
        this.treeService = treeService;
    }

    @PostMapping
    public ResponseEntity<Tree> createTree(@Valid @RequestBody Tree tree) {
        return ResponseEntity.status(201).body(treeService.createTree(tree));
    }

    @GetMapping
    public ResponseEntity<List<Tree>> getAllTrees() {
        return ResponseEntity.ok(treeService.getAllTrees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tree> getTreeById(@PathVariable Long id) {
        return ResponseEntity.ok(treeService.getTreeById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tree> updateTree(@PathVariable Long id,
                                            @Valid @RequestBody Tree tree) {
        return ResponseEntity.ok(treeService.updateTree(id, tree));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTree(@PathVariable Long id) {
        treeService.deleteTree(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/species/{species}")
    public ResponseEntity<List<Tree>> getTreesBySpecies(@PathVariable String species) {
        return ResponseEntity.ok(treeService.getTreesBySpecies(species));
    }

    @GetMapping("/species/{species}/survival-rate")
    public ResponseEntity<Double> getSpeciesSurvivalRate(@PathVariable String species) {
        return ResponseEntity.ok(treeService.getSpeciesSurvivalRate(species));
    }

    @GetMapping("/volunteer/{volunteerId}")
    public ResponseEntity<List<Tree>> getTreesByVolunteer(@PathVariable Long volunteerId) {
        return ResponseEntity.ok(treeService.getTreesByVolunteer(volunteerId));
    }

    @GetMapping("/drive/{driveId}")
    public ResponseEntity<List<Tree>> getTreesByDrive(@PathVariable Long driveId) {
        return ResponseEntity.ok(treeService.getTreesByDrive(driveId));
    }

    @GetMapping("/due-for-checkin")
    public ResponseEntity<List<Tree>> getTreesDueForCheckIn() {
        return ResponseEntity.ok(treeService.getTreesDueForCheckIn());
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<Map<String, Long>> getVolunteerLeaderboard() {
        return ResponseEntity.ok(treeService.getVolunteerLeaderboard());
    }
}
