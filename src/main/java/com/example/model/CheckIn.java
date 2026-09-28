package com.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "check_ins")
public class CheckIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Check-in date is required")
    private LocalDate checkInDate;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    private CheckInStatus status;

    private String notes;

    @NotNull(message = "Tree is required")
    @ManyToOne
    @JoinColumn(name = "tree_id", nullable = false)
    private Tree tree;

    public enum CheckInStatus {
        ALIVE,
        DEAD
    }

    public CheckIn() {
    }

    public CheckIn(Long id, LocalDate checkInDate, CheckInStatus status, String notes, Tree tree) {
        this.id = id;
        this.checkInDate = checkInDate;
        this.status = status;
        this.notes = notes;
        this.tree = tree;
    }

    public CheckIn(LocalDate checkInDate, CheckInStatus status, String notes, Tree tree) {
        this.checkInDate = checkInDate;
        this.status = status;
        this.notes = notes;
        this.tree = tree;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public void setCheckInDate(LocalDate checkInDate) { this.checkInDate = checkInDate; }
    public CheckInStatus getStatus() { return status; }
    public void setStatus(CheckInStatus status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Tree getTree() { return tree; }
    public void setTree(Tree tree) { this.tree = tree; }

    @Override
    public String toString() {
        return "CheckIn{" + "id=" + id + ", checkInDate=" + checkInDate + ", status=" + status + '}';
    }
}
