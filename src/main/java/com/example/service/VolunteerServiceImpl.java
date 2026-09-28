package com.example.service;

import com.example.model.Volunteer;
import com.example.repo.VolunteerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VolunteerServiceImpl implements VolunteerService {

    private final VolunteerRepository volunteerRepository;

    public VolunteerServiceImpl(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    @Override
    public Volunteer createVolunteer(Volunteer volunteer) {
        volunteer.setId(null);
        return volunteerRepository.save(volunteer);
    }

    @Override
    public Volunteer getVolunteerById(Long id) {
        return volunteerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));
    }

    @Override
    public List<Volunteer> getAllVolunteers() {
        return volunteerRepository.findAll();
    }

    @Override
    public Volunteer updateVolunteer(Long id, Volunteer volunteer) {
        Volunteer existing = getVolunteerById(id);
        existing.setName(volunteer.getName());
        existing.setEmail(volunteer.getEmail());
        return volunteerRepository.save(existing);
    }

    @Override
    public void deleteVolunteer(Long id) {
        Volunteer existing = getVolunteerById(id);
        volunteerRepository.delete(existing);
    }
}

