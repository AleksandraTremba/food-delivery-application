package com.fooddelivery.fooddelivery.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity class representing fees associated with weather phenomenon fees (WPE).
 * This entity is mapped to the "WPEF_FEES" table in the database and contains information
 * about the vehicle type, weather condition, and the corresponding fee.
 */
@Entity
@Table(name = "WPEF_FEES")
@Getter
public class WPEFFees {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String vehicle;

    @Setter
    @Column(nullable = false)
    private String weather;

    @Setter
    @Column(nullable = false)
    private Double fee;
}
