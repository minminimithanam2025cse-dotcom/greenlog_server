package com.example.service;

import com.example.model.CheckIn;

import java.util.List;

public interface CheckInService {
    CheckIn createCheckIn(CheckIn checkIn);
    CheckIn getCheckInById(Long id);
    List<CheckIn> getAllCheckIns();
    List<CheckIn> getCheckInsByTree(Long treeId);
    void deleteCheckIn(Long id);
}
