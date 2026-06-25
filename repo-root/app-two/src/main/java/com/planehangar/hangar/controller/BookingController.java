package com.planehangar.hangar.controller;

import com.planehangar.hangar.client.AircraftClient;
import com.planehangar.hangar.dto.BookingRequest;
import com.planehangar.hangar.model.Booking;
import com.planehangar.hangar.model.HangarBay;
import com.planehangar.hangar.repository.BookingRepository;
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

/*rEST endpoints for managing bookings of aircraft into hangar bays.
 * GET    /bookings       - list all bookings
 * GET    /bookings/{id}  - get a single booking
 * POST   /bookings       - create a new booking
 * PUT    /bookings/{id}  - update an existing booking
 * DELETE /bookings/{id}  - delete a booking*/
@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingRepository bookingRepository;
    private final HangarBayRepository hangarBayRepository;
    private final AircraftClient aircraftClient;

    public BookingController(BookingRepository bookingRepository,
                              HangarBayRepository hangarBayRepository,
                              AircraftClient aircraftClient) {
        this.bookingRepository = bookingRepository;
        this.hangarBayRepository = hangarBayRepository;
        this.aircraftClient = aircraftClient;
    }

    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        return bookingRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request) {
        return hangarBayRepository.findById(request.getBayId())
                .<ResponseEntity<?>>map(bay -> {
                    if (aircraftClient.getAircraft(request.getAircraftId()).isEmpty()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("No aircraft found with id " + request.getAircraftId());
                    }
                    Booking booking = new Booking(
                            request.getAircraftId(),
                            bay,
                            request.getStartDate(),
                            request.getEndDate());
                    Booking saved = bookingRepository.save(booking);
                    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("No hangar bay found with id " + request.getBayId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Long id, @RequestBody BookingRequest request) {
        return bookingRepository.findById(id)
                .<ResponseEntity<?>>map(existing -> {
                    HangarBay bay = hangarBayRepository.findById(request.getBayId()).orElse(null);
                    if (bay == null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("No hangar bay found with id " + request.getBayId());
                    }
                    if (aircraftClient.getAircraft(request.getAircraftId()).isEmpty()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("No aircraft found with id " + request.getAircraftId());
                    }
                    existing.setAircraftId(request.getAircraftId());
                    existing.setHangarBay(bay);
                    existing.setStartDate(request.getStartDate());
                    existing.setEndDate(request.getEndDate());
                    Booking saved = bookingRepository.save(existing);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        if (!bookingRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        bookingRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
