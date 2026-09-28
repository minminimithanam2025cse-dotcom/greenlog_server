package com.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "plantation_drives")
public class PlantationDrive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Date is required")
    private LocalDate date;

    private String description;

    public PlantationDrive() {
    }

    public PlantationDrive(Long id, String name, String location, LocalDate date, String description) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.date = date;
        this.description = description;
    }

    public PlantationDrive(String name, String location, LocalDate date, String description) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "PlantationDrive{" + "id=" + id + ", name='" + name + '\'' + ", location='" + location + '\'' + ", date=" + date + '}';
    }
}
