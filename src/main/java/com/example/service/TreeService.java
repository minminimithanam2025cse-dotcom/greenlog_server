package com.example.service;

import com.example.model.Tree;

import java.util.List;
import java.util.Map;

public interface TreeService {
    Tree createTree(Tree tree);
    Tree getTreeById(Long id);
    List<Tree> getAllTrees();
    Tree updateTree(Long id, Tree tree);
    void deleteTree(Long id);
    List<Tree> getTreesBySpecies(String species);
    List<Tree> getTreesByVolunteer(Long volunteerId);
    List<Tree> getTreesByDrive(Long driveId);
    double getSpeciesSurvivalRate(String species);
    List<Tree> getTreesDueForCheckIn();
    Map<String, Long> getVolunteerLeaderboard();
}
