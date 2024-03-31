package com.fooddelivery.fooddelivery.repository;

import com.fooddelivery.fooddelivery.entities.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repository interface for accessing weather station data.
 */
public interface StationRepository extends JpaRepository<Station, Long> {
    Optional<Station> findFirstByNameOrderByTimestampDesc(String name);
    Optional<Station> findFirstByTimestampOrderByTimestampDesc(LocalDateTime dateTime);
}
