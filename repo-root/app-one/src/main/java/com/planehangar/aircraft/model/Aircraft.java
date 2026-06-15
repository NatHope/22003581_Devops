package com.planehangar.aircraft.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Represents an aircraft owned or managed by the hangar.
 */
@Entity
@Table(name = "aircraft")
public class Aircraft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tail_number", nullable = false, unique = true)
    private String tailNumber;

    @Column(nullable = false)
    private String model;

    private String manufacturer;

    private Integer capacity;

    /**
     * One of AIRWORTHY, MAINTENANCE, GROUNDED.
     */
    @Column(nullable = false)
    private String status;

    public Aircraft() {
    }

    public Aircraft(String tailNumber, String model, String manufacturer, Integer capacity, String status) {
        this.tailNumber = tailNumber;
        this.model = model;
        this.manufacturer = manufacturer;
        this.capacity = capacity;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTailNumber() {
        return tailNumber;
    }

    public void setTailNumber(String tailNumber) {
        this.tailNumber = tailNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
