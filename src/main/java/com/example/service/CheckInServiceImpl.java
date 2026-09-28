package com.example.service;

import com.example.model.CheckIn;
import com.example.model.Tree;
import com.example.repo.CheckInRepository;
import com.example.repo.TreeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckInServiceImpl implements CheckInService {

    private final CheckInRepository checkInRepository;
    private final TreeRepository treeRepository;

    public CheckInServiceImpl(CheckInRepository checkInRepository,
                              TreeRepository treeRepository) {
        this.checkInRepository = checkInRepository;
        this.treeRepository = treeRepository;
    }

    @Override
    public CheckIn createCheckIn(CheckIn checkIn) {
        if (checkIn.getTree() == null || checkIn.getTree().getId() == null) {
            throw new RuntimeException("Tree ID is required");
        }

        Tree tree = treeRepository.findById(checkIn.getTree().getId())
                .orElseThrow(() -> new RuntimeException("Tree not found"));

        CheckIn latestCheckIn = checkInRepository.findFirstByTreeOrderByCheckInDateDesc(tree).orElse(null);
        if (latestCheckIn != null && latestCheckIn.getStatus() == CheckIn.CheckInStatus.DEAD) {
            throw new RuntimeException("Cannot add check-in. Tree is already dead.");
        }

        checkIn.setId(null);
        checkIn.setTree(tree);
        return checkInRepository.save(checkIn);
    }

    @Override
    public CheckIn getCheckInById(Long id) {
        return checkInRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Check-in not found"));
    }

    @Override
    public List<CheckIn> getAllCheckIns() {
        return checkInRepository.findAll();
    }

    @Override
    public List<CheckIn> getCheckInsByTree(Long treeId) {
        treeRepository.findById(treeId)
                .orElseThrow(() -> new RuntimeException("Tree not found"));
        return checkInRepository.findByTreeIdOrderByCheckInDateDesc(treeId);
    }

    @Override
    public void deleteCheckIn(Long id) {
        CheckIn existing = getCheckInById(id);
        checkInRepository.delete(existing);
    }
}

