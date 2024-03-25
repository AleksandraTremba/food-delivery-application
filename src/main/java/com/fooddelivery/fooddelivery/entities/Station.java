package com.fooddelivery.fooddelivery.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "STATIONS")
@Getter
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Setter
    @Column(nullable = false)
    private Integer WMO;

    @Setter
    @Column(nullable = false)
    private Integer temperature;

    @Setter
    @Column(nullable = false)
    private Integer windSpeed;

    @Setter
    @Column(nullable = false)
    private String weatherPhenomenon;

    @Setter
    @Column(nullable = false)
    private LocalDateTime timestamp;
}
