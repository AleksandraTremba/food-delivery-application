package com.fooddelivery.fooddelivery.tests;

import com.fooddelivery.fooddelivery.entities.ATEFFees;
import com.fooddelivery.fooddelivery.entities.RBFFees;
import com.fooddelivery.fooddelivery.entities.WPEFFees;
import com.fooddelivery.fooddelivery.entities.WSEFFees;
import com.fooddelivery.fooddelivery.repository.ATEFFeeRepository;
import com.fooddelivery.fooddelivery.repository.RBFFeeRepository;
import com.fooddelivery.fooddelivery.repository.WPEFFeeRepository;
import com.fooddelivery.fooddelivery.repository.WSEFFeeRepository;
import com.fooddelivery.fooddelivery.services.UpdateFeeDataService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class FeeUpdatingTest {
    @Mock
    private RBFFeeRepository rbfFeesRepository;
    @Mock
    private ATEFFeeRepository atefFeesRepository;
    @Mock
    private WSEFFeeRepository wsefFeesRepository;
    @Mock
    private WPEFFeeRepository wpefFeesRepository;

    @InjectMocks
    private UpdateFeeDataService updateFeeDataService;

    public FeeUpdatingTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateRBFFees_SavesCorrectly() {
        String city = "Tallinn";
        String vehicle = "Bike";
        Double fee = 3.0;

        ArgumentCaptor<RBFFees> captor = ArgumentCaptor.forClass(RBFFees.class);

        updateFeeDataService.updateRBFFees(city, vehicle, fee);

        verify(rbfFeesRepository).save(captor.capture());
        RBFFees savedRBFFees = captor.getValue();

        assertEquals(city, savedRBFFees.getCity());
        assertEquals(vehicle, savedRBFFees.getVehicle());
        assertEquals(fee, savedRBFFees.getFee());
    }

    @Test
    void updateATEFFees_SavesCorrectly() {
        String vehicle = "Bike";
        Double temperature = -10.0;
        Double fee = 3.0;

        ArgumentCaptor<ATEFFees> captor = ArgumentCaptor.forClass(ATEFFees.class);

        updateFeeDataService.updateATEFFees(vehicle, temperature, fee);

        verify(atefFeesRepository).save(captor.capture());
        ATEFFees savedATEFFees = captor.getValue();

        assertEquals(vehicle, savedATEFFees.getVehicle());
        assertEquals(temperature, savedATEFFees.getTemperature());
        assertEquals(fee, savedATEFFees.getFee());
    }

    @Test
    void updateWSEFFees_SavesCorrectly() {
        String vehicle = "Bike";
        Double speed = 15.0;
        Double fee = 0.5;

        ArgumentCaptor<WSEFFees> captor = ArgumentCaptor.forClass(WSEFFees.class);

        updateFeeDataService.updateWSEFFees(vehicle, speed, fee);

        verify(wsefFeesRepository).save(captor.capture());
        WSEFFees savedWSEFFees = captor.getValue();

        assertEquals(vehicle, savedWSEFFees.getVehicle());
        assertEquals(speed, savedWSEFFees.getSpeed());
        assertEquals(fee, savedWSEFFees.getFee());
    }

    @Test
    void updateWPEFFees_SavesCorrectly() {
        String vehicle = "Bike";
        String weather = "sunny";
        Double fee = 3.0;

        ArgumentCaptor<WPEFFees> captor = ArgumentCaptor.forClass(WPEFFees.class);

        updateFeeDataService.updateWPEFFees(vehicle, weather, fee);

        verify(wpefFeesRepository).save(captor.capture());
        WPEFFees savedWPEFFees = captor.getValue();

        assertEquals(vehicle, savedWPEFFees.getVehicle());
        assertEquals(weather, savedWPEFFees.getWeather());
        assertEquals(fee, savedWPEFFees.getFee());
    }
}
