package com.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "trees")
public class Tree {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Species is required")
    private String species;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Date planted is required")
    private LocalDate datePlanted;

    @ManyToOne
    @JoinColumn(name = "volunteer_id", nullable = false)
    private Volunteer volunteer;

    @ManyToOne
    @JoinColumn(name = "plantation_drive_id", nullable = false)
    private PlantationDrive plantationDrive;

    public Tree() {
    }

    public Tree(Long id, String species, String location, LocalDate datePlanted,
                Volunteer volunteer, PlantationDrive plantationDrive) {
        this.id = id;
        this.species = species;
        this.location = location;
        this.datePlanted = datePlanted;
        this.volunteer = volunteer;
        this.plantationDrive = plantationDrive;
    }

    public Tree(String species, String location, LocalDate datePlanted,
                Volunteer volunteer, PlantationDrive plantationDrive) {
        this.species = species;
        this.location = location;
        this.datePlanted = datePlanted;
        this.volunteer = volunteer;
        this.plantationDrive = plantationDrive;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public LocalDate getDatePlanted() { return datePlanted; }
    public void setDatePlanted(LocalDate datePlanted) { this.datePlanted = datePlanted; }
    public Volunteer getVolunteer() { return volunteer; }
    public void setVolunteer(Volunteer volunteer) { this.volunteer = volunteer; }
    public PlantationDrive getPlantationDrive() { return plantationDrive; }
    public void setPlantationDrive(PlantationDrive plantationDrive) { this.plantationDrive = plantationDrive; }

    @Override
    public String toString() {
        return "Tree{" + "id=" + id + ", species='" + species + '\'' + ", location='" + location + '\'' + ", datePlanted=" + datePlanted + '}';
    }
}
