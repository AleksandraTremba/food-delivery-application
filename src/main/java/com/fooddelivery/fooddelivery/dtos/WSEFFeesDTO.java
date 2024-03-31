package com.fooddelivery.fooddelivery.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing wind speed fees (WSE).
 * This DTO contains information about the vehicle type, wind speed, and the corresponding fee.
 */
@Getter
@Setter
public class WSEFFeesDTO {
    private Long id;
    private String vehicle;
    private Double speed;
    private Double fee;
}
