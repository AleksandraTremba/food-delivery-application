package com.fooddelivery.fooddelivery.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity class representing fees associated with wind speed fees (WSE).
 * This entity is mapped to the "WSEF_FEES" table in the database and contains information
 * about the vehicle type, wind speed, and the corresponding fee.
 */
@Entity
@Table(name = "WSEF_FEES")
@Getter
public class WSEFFees {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String vehicle;

    @Setter
    @Column(nullable = false)
    private Double speed;

    @Setter
    @Column(nullable = false)
    private Double fee;
}
