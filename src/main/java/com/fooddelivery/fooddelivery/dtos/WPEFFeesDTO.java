package com.fooddelivery.fooddelivery.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing weather phenomenon fees (WPE).
 * This DTO contains information about the vehicle type, weather, and the corresponding fee.
 */
@Getter
@Setter
public class WPEFFeesDTO {
    private Long id;
    private String vehicle;
    private String weather;
    private Double fee;

}
