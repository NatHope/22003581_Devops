package com.planehangar.aircraft.controller;

import com.planehangar.aircraft.model.Aircraft;
import com.planehangar.aircraft.repository.AircraftRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*REST endpoints for managing aircraft.
 * GET    /aircraft       - list all aircraft
 * GET    /aircraft/{id}  - get a single aircraft
 * POST   /aircraft       - create a new aircraft
 * PUT    /aircraft/{id}  - update an existing aircraft
 * DELETE /aircraft/{id}  - delete an aircraft*/
@RestController
@RequestMapping("/aircraft")
@CrossOrigin(origins = "*")
public class AircraftController {

    private final AircraftRepository repository;

    public AircraftController(AircraftRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Aircraft> getAllAircraft() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aircraft> getAircraftById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Aircraft> createAircraft(@RequestBody Aircraft aircraft) {
        Aircraft saved = repository.save(aircraft);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aircraft> updateAircraft(@PathVariable Long id, @RequestBody Aircraft updated) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setTailNumber(updated.getTailNumber());
                    existing.setModel(updated.getModel());
                    existing.setManufacturer(updated.getManufacturer());
                    existing.setCapacity(updated.getCapacity());
                    existing.setStatus(updated.getStatus());
                    Aircraft saved = repository.save(existing);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAircraft(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
