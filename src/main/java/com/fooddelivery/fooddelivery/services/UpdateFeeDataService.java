package com.fooddelivery.fooddelivery.services;

import com.fooddelivery.fooddelivery.entities.ATEFFees;
import com.fooddelivery.fooddelivery.entities.RBFFees;
import com.fooddelivery.fooddelivery.entities.WPEFFees;
import com.fooddelivery.fooddelivery.entities.WSEFFees;
import com.fooddelivery.fooddelivery.repository.ATEFFeeRepository;
import com.fooddelivery.fooddelivery.repository.RBFFeeRepository;
import com.fooddelivery.fooddelivery.repository.WPEFFeeRepository;
import com.fooddelivery.fooddelivery.repository.WSEFFeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service class for updating fee data in the database.
 */
@Service
@RequiredArgsConstructor
public class UpdateFeeDataService {
    private final RBFFeeRepository rbfFeesRepository;
    private final ATEFFeeRepository atefFeesRepository;
    private final WSEFFeeRepository wsefFeesRepository;
    private final WPEFFeeRepository wpefFeesRepository;

    /**
     * Updates the RBF for the specified city and vehicle.
     *
     * @param city    The city for which the fee is being updated.
     * @param vehicle The vehicle for which the fee is being updated.
     * @param fee     The fee to be updated.
     */
    public void updateRBFFees(String city, String vehicle, Double fee) {
        if (city != null && vehicle != null && fee != null) {
            RBFFees rbfFees = new RBFFees();
            rbfFees.setCity(city);
            rbfFees.setVehicle(vehicle);
            rbfFees.setFee(fee);
            rbfFeesRepository.save(rbfFees);
        } else {
            throw new IllegalArgumentException("City, vehicle, and fee value cannot be null");
        }
    }

    /**
     * Updates the ATEF for the specified vehicle and temperature.
     *
     * @param vehicle     The vehicle for which the fee is being updated.
     * @param temperature The temperature for which the fee is being updated.
     * @param fee         The fee to be updated.
     */
    public void updateATEFFees(String vehicle, Double temperature, Double fee) {
        if (vehicle != null && temperature != null && fee != null) {
            ATEFFees atefFees = new ATEFFees();
            atefFees.setVehicle(vehicle);
            atefFees.setTemperature(temperature);
            atefFees.setFee(fee);
            atefFeesRepository.save(atefFees);
        } else {
            throw new IllegalArgumentException("Vehicle, temperature, and fee value cannot be null");
        }
    }

    /**
     * Updates the WSEF for the specified vehicle and speed.
     *
     * @param vehicle The vehicle for which the fee is being updated.
     * @param speed   The speed for which the fee is being updated.
     * @param fee     The fee to be updated.
     */
    public void updateWSEFFees(String vehicle, Double speed, Double fee) {
        if (vehicle != null && speed != null && fee != null) {
            WSEFFees wsefFees = new WSEFFees();
            wsefFees.setVehicle(vehicle);
            wsefFees.setSpeed(speed);
            wsefFees.setFee(fee);
            wsefFeesRepository.save(wsefFees);
        } else {
            throw new IllegalArgumentException("Vehicle, speed, and fee value cannot be null");
        }
    }

    /**
     * Updates the WPEF for the specified vehicle and weather condition.
     *
     * @param vehicle The vehicle for which the fee is being updated.
     * @param weather The weather condition for which the fee is being updated.
     * @param fee     The fee to be updated.
     */
    public void updateWPEFFees(String vehicle, String weather, Double fee) {
        if (vehicle != null && weather != null && fee != null) {
            WPEFFees wpefFees = new WPEFFees();
            wpefFees.setVehicle(vehicle);
            wpefFees.setWeather(weather);
            wpefFees.setFee(fee);
            wpefFeesRepository.save(wpefFees);
        } else {
            throw new IllegalArgumentException("Vehicle, weather, and fee value cannot be null");
        }
    }
}
