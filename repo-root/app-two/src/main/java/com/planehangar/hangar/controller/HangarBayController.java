package com.planehangar.hangar.controller;

import com.planehangar.hangar.model.HangarBay;
import com.planehangar.hangar.repository.HangarBayRepository;
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

/*
 * REST endpoints for managing hangar bays.
 * GET    /bays       - list all hangar bays
 * GET    /bays/{id}  - get a single hangar bay
 * POST   /bays       - create a new hangar bay
 * PUT    /bays/{id}  - update an existing hangar bay
 * DELETE /bays/{id}  - delete a hangar bay
 */
@RestController
@RequestMapping("/bays")
@CrossOrigin(origins = "*")
public class HangarBayController {

    private final HangarBayRepository repository;

    public HangarBayController(HangarBayRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<HangarBay> getAllBays() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HangarBay> getBayById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<HangarBay> createBay(@RequestBody HangarBay bay) {
        HangarBay saved = repository.save(bay);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HangarBay> updateBay(@PathVariable Long id, @RequestBody HangarBay updated) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setBayNumber(updated.getBayNumber());
                    existing.setLocation(updated.getLocation());
                    existing.setCapacity(updated.getCapacity());
                    HangarBay saved = repository.save(existing);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBay(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
