package com.example.service;

import com.example.model.CheckIn;
import com.example.model.Tree;
import com.example.model.Volunteer;
import com.example.model.PlantationDrive;
import com.example.repo.CheckInRepository;
import com.example.repo.PlantationDriveRepository;
import com.example.repo.TreeRepository;
import com.example.repo.VolunteerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TreeServiceImpl implements TreeService {

    private static final long CHECK_IN_INTERVAL_DAYS = 30;

    private final TreeRepository treeRepository;
    private final VolunteerRepository volunteerRepository;
    private final PlantationDriveRepository plantationDriveRepository;
    private final CheckInRepository checkInRepository;

    public TreeServiceImpl(TreeRepository treeRepository,
                           VolunteerRepository volunteerRepository,
                           PlantationDriveRepository plantationDriveRepository,
                           CheckInRepository checkInRepository) {
        this.treeRepository = treeRepository;
        this.volunteerRepository = volunteerRepository;
        this.plantationDriveRepository = plantationDriveRepository;
        this.checkInRepository = checkInRepository;
    }

    @Override
    public Tree createTree(Tree tree) {
        validateRelatedEntities(tree);
        tree.setId(null);
        tree.setVolunteer(findVolunteer(tree.getVolunteer()));
        tree.setPlantationDrive(findDrive(tree.getPlantationDrive()));
        return treeRepository.save(tree);
    }

    @Override
    public Tree getTreeById(Long id) {
        return treeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tree not found"));
    }

    @Override
    public List<Tree> getAllTrees() {
        return treeRepository.findAll();
    }

    @Override
    public Tree updateTree(Long id, Tree tree) {
        Tree existing = getTreeById(id);
        validateRelatedEntities(tree);

        existing.setSpecies(tree.getSpecies());
        existing.setLocation(tree.getLocation());
        existing.setDatePlanted(tree.getDatePlanted());
        existing.setVolunteer(findVolunteer(tree.getVolunteer()));
        existing.setPlantationDrive(findDrive(tree.getPlantationDrive()));
        return treeRepository.save(existing);
    }

    @Override
    public void deleteTree(Long id) {
        Tree existing = getTreeById(id);
        treeRepository.delete(existing);
    }

    @Override
    public List<Tree> getTreesBySpecies(String species) {
        return treeRepository.findBySpeciesIgnoreCase(species);
    }

    @Override
    public List<Tree> getTreesByVolunteer(Long volunteerId) {
        volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));
        return treeRepository.findByVolunteerId(volunteerId);
    }

    @Override
    public List<Tree> getTreesByDrive(Long driveId) {
        plantationDriveRepository.findById(driveId)
                .orElseThrow(() -> new RuntimeException("Plantation drive not found"));
        return treeRepository.findByPlantationDriveId(driveId);
    }

    @Override
    public double getSpeciesSurvivalRate(String species) {
        List<Tree> trees = getTreesBySpecies(species);
        if (trees.isEmpty()) {
            return 0.0;
        }

        long aliveTrees = trees.stream()
                .filter(this::isLatestStatusAlive)
                .count();
        return (aliveTrees * 100.0) / trees.size();
    }

    @Override
    public List<Tree> getTreesDueForCheckIn() {
        LocalDate today = LocalDate.now();

        return treeRepository.findAll().stream()
                .filter(tree -> {
                    CheckIn latest = checkInRepository.findFirstByTreeOrderByCheckInDateDesc(tree).orElse(null);
                    if (latest != null && latest.getStatus() == CheckIn.CheckInStatus.DEAD) {
                        return false;
                    }

                    LocalDate baseDate = latest == null ? tree.getDatePlanted() : latest.getCheckInDate();
                    LocalDate nextCheckIn = baseDate.plusDays(CHECK_IN_INTERVAL_DAYS);
                    return !nextCheckIn.isAfter(today);
                })
                .toList();
    }

    @Override
    public Map<String, Long> getVolunteerLeaderboard() {
        return treeRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        tree -> tree.getVolunteer().getName(),
                        Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (first, second) -> first,
                        LinkedHashMap::new));
    }

    private boolean isLatestStatusAlive(Tree tree) {
        return checkInRepository.findFirstByTreeOrderByCheckInDateDesc(tree)
                .map(CheckIn::getStatus)
                .map(CheckIn.CheckInStatus.ALIVE::equals)
                .orElse(false);
    }

    private void validateRelatedEntities(Tree tree) {
        if (tree.getVolunteer() == null || tree.getVolunteer().getId() == null) {
            throw new RuntimeException("Volunteer ID is required");
        }
        if (tree.getPlantationDrive() == null || tree.getPlantationDrive().getId() == null) {
            throw new RuntimeException("Plantation drive ID is required");
        }
    }

    private Volunteer findVolunteer(Volunteer volunteer) {
        return volunteerRepository.findById(volunteer.getId())
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));
    }

    private PlantationDrive findDrive(PlantationDrive drive) {
        return plantationDriveRepository.findById(drive.getId())
                .orElseThrow(() -> new RuntimeException("Plantation drive not found"));
    }
}
