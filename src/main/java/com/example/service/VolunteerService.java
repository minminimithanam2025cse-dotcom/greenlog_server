package com.example.service;

import com.example.model.Volunteer;

import java.util.List;

public interface VolunteerService {
    Volunteer createVolunteer(Volunteer volunteer);
    Volunteer getVolunteerById(Long id);
    List<Volunteer> getAllVolunteers();
    Volunteer updateVolunteer(Long id, Volunteer volunteer);
    void deleteVolunteer(Long id);
}
