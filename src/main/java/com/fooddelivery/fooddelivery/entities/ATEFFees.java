package com.fooddelivery.fooddelivery.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity class representing fees associated with air temperature fees (ATE).
 * This entity is mapped to the "ATEF_FEES" table in the database and contains information
 * about the vehicle type, temperature, and the corresponding fee.
 */
@Entity
@Table(name = "ATEF_FEES")
@Getter
public class ATEFFees {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String vehicle;

    @Setter
    @Column(nullable = false)
    private Double temperature;

    @Setter
    @Column(nullable = false)
    private Double fee;
}
