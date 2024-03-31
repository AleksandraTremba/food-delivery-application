package com.fooddelivery.fooddelivery.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity class representing fees associated with regional base fees (RBF).
 * This entity is mapped to the "RBF_FEES" table in the database and contains information
 * about the city, vehicle type, and the corresponding fee.
 */
@Entity
@Table(name = "RBF_FEES")
@Getter
@NoArgsConstructor
public class RBFFees {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String city;

    @Setter
    @Column(nullable = false)
    private String vehicle;

    @Setter
    @Column(nullable = false)
    private Double fee;
}
