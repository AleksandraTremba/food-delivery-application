package com.fooddelivery.fooddelivery.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing regional base fees (RBF).
 * This DTO contains information about the city, vehicle type, and the corresponding fee.
 */
@Getter
@Setter
public class RBFFeesDTO {
    private Long id;
    private String city;
    private String vehicle;
    private Double fee;
}