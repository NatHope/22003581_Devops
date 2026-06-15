package com.planehangar.aircraft.repository;

import com.planehangar.aircraft.model.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AircraftRepository extends JpaRepository<Aircraft, Long> {
}
