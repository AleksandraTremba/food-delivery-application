package com.fooddelivery.fooddelivery.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing air temperature fees (ATE).
 * This DTO contains information about the vehicle type, temperature, and the corresponding fee.
 */
@Getter
@Setter
public class ATEFFeesDTO {
    private Long id;
    private String vehicle;
    private Double temperature;
    private Double fee;

}