package com.example.repo;

import com.example.model.Tree;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TreeRepository extends JpaRepository<Tree, Long> {

    List<Tree> findBySpeciesIgnoreCase(String species);

    List<Tree> findByVolunteerId(Long volunteerId);

    List<Tree> findByPlantationDriveId(Long plantationDriveId);
}
