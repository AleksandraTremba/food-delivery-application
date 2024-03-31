package com.fooddelivery.fooddelivery.repository;

import com.fooddelivery.fooddelivery.entities.WSEFFees;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
/**
 * Repository interface for accessing wind speed fees (WSE) fee data.
 */
public interface WSEFFeeRepository extends JpaRepository<WSEFFees, Long> {
    Optional<WSEFFees> findBySpeed(double temperature);
}
