package com.planehangar.hangar.repository;

import com.planehangar.hangar.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
