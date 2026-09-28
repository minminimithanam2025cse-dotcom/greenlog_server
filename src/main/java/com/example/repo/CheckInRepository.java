package com.example.repo;

import com.example.model.CheckIn;
import com.example.model.Tree;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CheckInRepository extends JpaRepository<CheckIn, Long> {
    List<CheckIn> findByTreeIdOrderByCheckInDateDesc(Long treeId);

    Optional<CheckIn> findFirstByTreeOrderByCheckInDateDesc(Tree tree);
}
