package com.example.service;

import com.example.model.CheckIn;
import com.example.model.PlantationDrive;
import com.example.model.Tree;
import com.example.repo.CheckInRepository;
import com.example.repo.PlantationDriveRepository;
import com.example.repo.TreeRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class PlantationDriveServiceImpl implements PlantationDriveService {

    private final PlantationDriveRepository plantationDriveRepository;
    private final TreeRepository treeRepository;
    private final CheckInRepository checkInRepository;

    public PlantationDriveServiceImpl(PlantationDriveRepository plantationDriveRepository,
                                      TreeRepository treeRepository,
                                      CheckInRepository checkInRepository) {
        this.plantationDriveRepository = plantationDriveRepository;
        this.treeRepository = treeRepository;
        this.checkInRepository = checkInRepository;
    }

    @Override
    public PlantationDrive createDrive(PlantationDrive drive) {
        drive.setId(null);
        return plantationDriveRepository.save(drive);
    }

    @Override
    public PlantationDrive getDriveById(Long id) {
        return plantationDriveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plantation drive not found"));
    }

    @Override
    public List<PlantationDrive> getAllDrives() {
        return plantationDriveRepository.findAll();
    }

    @Override
    public PlantationDrive updateDrive(Long id, PlantationDrive drive) {
        PlantationDrive existing = getDriveById(id);
        existing.setName(drive.getName());
        existing.setLocation(drive.getLocation());
        existing.setDate(drive.getDate());
        existing.setDescription(drive.getDescription());
        return plantationDriveRepository.save(existing);
    }

    @Override
    public void deleteDrive(Long id) {
        PlantationDrive existing = getDriveById(id);
        plantationDriveRepository.delete(existing);
    }

    @Override
    public double getSurvivalRate(Long driveId) {
        getDriveById(driveId);
        List<Tree> trees = treeRepository.findByPlantationDriveId(driveId);
        if (trees.isEmpty()) {
            return 0.0;
        }

        long aliveTrees = countAliveTrees(trees);
        return (aliveTrees * 100.0) / trees.size();
    }

    @Override
    public Map<String, Object> getDriveStatistics(Long driveId) {
        PlantationDrive drive = getDriveById(driveId);
        List<Tree> trees = treeRepository.findByPlantationDriveId(driveId);

        long aliveTrees = countAliveTrees(trees);
        long deadTrees = trees.stream()
                .filter(tree -> isLatestStatusDead(tree))
                .count();
        double survivalRate = trees.isEmpty() ? 0.0 : (aliveTrees * 100.0) / trees.size();

        Map<String, Object> statistics = new LinkedHashMap<>();
        statistics.put("driveId", drive.getId());
        statistics.put("driveName", drive.getName());
        statistics.put("totalTrees", trees.size());
        statistics.put("aliveTrees", aliveTrees);
        statistics.put("deadTrees", deadTrees);
        statistics.put("survivalRate", survivalRate);
        return statistics;
    }

    private long countAliveTrees(List<Tree> trees) {
        return trees.stream()
                .filter(this::isLatestStatusAlive)
                .count();
    }

    private boolean isLatestStatusAlive(Tree tree) {
        return checkInRepository.findFirstByTreeOrderByCheckInDateDesc(tree)
                .map(CheckIn::getStatus)
                .map(CheckIn.CheckInStatus.ALIVE::equals)
                .orElse(false);
    }

    private boolean isLatestStatusDead(Tree tree) {
        return checkInRepository.findFirstByTreeOrderByCheckInDateDesc(tree)
                .map(CheckIn::getStatus)
                .map(CheckIn.CheckInStatus.DEAD::equals)
                .orElse(false);
    }
}
