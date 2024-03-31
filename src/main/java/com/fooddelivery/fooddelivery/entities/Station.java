package com.fooddelivery.fooddelivery.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entity class representing weather stations.
 * This entity is mapped to the "STATIONS" table in the database and contains information
 * about weather station details such as name, WMO code, temperature, wind speed, weather phenomenon,
 * and timestamp of the recorded data.
 */
@Entity
@Table(name = "STATIONS")
@Getter
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_generator")
    @SequenceGenerator(name = "employee_generator", sequenceName = "employee_seq", allocationSize = 1)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @Column
    private Integer WMO;

    @Setter
    @Column
    private Double temperature;

    @Setter
    @Column(name = "WIND_SPEED")
    private Double windSpeed;

    @Setter
    @Column(name = "WEATHER_PHENOMENON")
    private String weatherPhenomenon;

    @Setter
    @Column(nullable = false)
    private LocalDateTime timestamp;
}
