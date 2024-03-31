package com.fooddelivery.fooddelivery.repository;

import com.fooddelivery.fooddelivery.entities.RBFFees;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for accessing regional base fees (RBF) fee data.
 */
public interface RBFFeeRepository extends JpaRepository<RBFFees, Long> {
    Optional<RBFFees> findByCityIgnoreCaseAndVehicleIgnoreCase(String city, String vehicle);

}
