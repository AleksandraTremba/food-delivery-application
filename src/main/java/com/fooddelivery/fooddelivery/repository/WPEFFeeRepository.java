package com.fooddelivery.fooddelivery.repository;

import com.fooddelivery.fooddelivery.entities.WPEFFees;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for accessing weather phenomenon fees (WPE) fee data.
 */
public interface WPEFFeeRepository extends JpaRepository<WPEFFees, Long> {
    Optional<WPEFFees> findFirstByVehicleIgnoreCaseAndWeatherIgnoreCase(String vehicle, String weather);

}
