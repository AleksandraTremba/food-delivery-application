package com.fooddelivery.fooddelivery.repository;

import com.fooddelivery.fooddelivery.entities.ATEFFees;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for accessing air temperature fees (ATE) fee data.
 */
public interface ATEFFeeRepository extends JpaRepository<ATEFFees, Long> {
    Optional<ATEFFees> findFirstByVehicleIgnoreCaseAndTemperature(String vehicle, double temperature);

}
