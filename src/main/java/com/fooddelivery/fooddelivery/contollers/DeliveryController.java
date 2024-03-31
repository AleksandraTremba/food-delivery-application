package com.fooddelivery.fooddelivery.contollers;


import com.fooddelivery.fooddelivery.dtos.ATEFFeesDTO;
import com.fooddelivery.fooddelivery.dtos.RBFFeesDTO;
import com.fooddelivery.fooddelivery.dtos.WPEFFeesDTO;
import com.fooddelivery.fooddelivery.dtos.WSEFFeesDTO;
import com.fooddelivery.fooddelivery.services.DeliveryService;
import com.fooddelivery.fooddelivery.services.UpdateFeeDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService deliveryService;
    private final UpdateFeeDataService updateFeeDataService;

    /**
     * Retrieves the fee for delivery based on city and vehicle.
     *
     * @param city    The city for delivery.
     * @param vehicle The type of vehicle used for delivery.
     * @return The total fee for delivery.
     * @throws IOException If wrong city/vehicle that is not providing service is entered.
     */
    @GetMapping("/{city}/{vehicle}")
    public double getFee(@PathVariable String city, @PathVariable String vehicle) throws IOException {
        return deliveryService.getTotalFee(city, vehicle, null);
    }
    /**
     * Retrieves the fee for delivery based on city, vehicle, and date.
     *
     * @param city    The city for delivery.
     * @param vehicle The type of vehicle used for delivery.
     * @param date    The date and time of delivery (optional).
     * @return The total fee for delivery.
     * @throws IOException If wrong city/vehicle/date that is not providing service is entered.
     */
    @GetMapping("/{city}/{vehicle}/{date}")
    public double getFee(@PathVariable String city, @PathVariable String vehicle, @PathVariable(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd-HH-mm-ss") LocalDateTime date) throws IOException {
        return deliveryService.getTotalFee(city, vehicle, date);
    }
    /**
     * Updates the fees for RBFFees. Adding this will allow it to be used in the fee calculation.
     *
     * @param rbFFeesDTO The RBFFeesDTO containing city, vehicle, and fee information.
     */
    @PutMapping("/rbf")
    public void updateRBFFees(@RequestBody RBFFeesDTO rbFFeesDTO) {
        updateFeeDataService.updateRBFFees(rbFFeesDTO.getCity(), rbFFeesDTO.getVehicle(), rbFFeesDTO.getFee());
    }

    /**
     * Updates the fees for ATEFFees.  Adding this will not allow it to be used in the fee calculation without additional conditions in DeliveryService.
     *
     * @param atEffeesDTO The ATEFFeesDTO containing vehicle, temperature, and fee information.
     */
    @PutMapping("/atef")
    public void updateATEFFees(@RequestBody ATEFFeesDTO atEffeesDTO) {
        updateFeeDataService.updateATEFFees(atEffeesDTO.getVehicle(), atEffeesDTO.getTemperature(), atEffeesDTO.getFee());
    }

    /**
     * Updates the fees for WSEFFees. Adding this will not allow it to be used in the fee calculation without additional conditions in DeliveryService.
     *
     * @param wsEffeesDTO The WSEFFeesDTO containing vehicle, speed, and fee information.
     */
    @PutMapping("/wsef")
    public void updateWSEFFees(@RequestBody WSEFFeesDTO wsEffeesDTO) {
        updateFeeDataService.updateWSEFFees(wsEffeesDTO.getVehicle(), wsEffeesDTO.getSpeed(), wsEffeesDTO.getFee());
    }

    /**
     * Updates the fees for WPEFFees. Adding this will allow it to be used in the fee calculation.
     *
     * @param wpEffeesDTO The WPEFFeesDTO containing vehicle, weather, and fee information.
     */
    @PutMapping("/wpef")
    public void updateWPEFFees(@RequestBody WPEFFeesDTO wpEffeesDTO) {
        updateFeeDataService.updateWPEFFees(wpEffeesDTO.getVehicle(), wpEffeesDTO.getWeather(), wpEffeesDTO.getFee());
    }
}
