package com.seller_details.model;

import jakarta.persistence.*;

@Entity
@Table(name = "status")
public class Status_idModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String statusName;
    private String description;

    public Status_idModel() {
    }

    public Status_idModel(Long id, String statusName, String description) {
        this.id = id;
        this.statusName = statusName;
        this.description = description;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
