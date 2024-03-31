package com.fooddelivery.fooddelivery.tests;

import com.fooddelivery.fooddelivery.entities.*;
import com.fooddelivery.fooddelivery.repository.*;
import com.fooddelivery.fooddelivery.services.DeliveryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FeeCalculationTest {
    @Mock
    private StationRepository stationRepository;

    @Mock
    private RBFFeeRepository rbfFeeRepository;
    @Mock
    private ATEFFeeRepository atefFeeRepository;

    @Mock
    private WSEFFeeRepository wsefFeeRepository;

    @Mock
    private WPEFFeeRepository wpefFeeRepository;

    @InjectMocks
    private DeliveryService deliveryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void task_test() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        when(stationRepository.findFirstByTimestampOrderByTimestampDesc(any(LocalDateTime.class))).thenReturn(Optional.of(createStation("Tartu", 123, 2.1, 4.7, "light snow shower", now)));
        when(stationRepository.findFirstByNameOrderByTimestampDesc(anyString())).thenReturn(Optional.of(createStation("Tartu", 123, 2.1, 4.7, "light snow shower", now)));

        RBFFees rbfFees = new RBFFees();
        rbfFees.setCity("Tartu");
        rbfFees.setVehicle("Bike");
        rbfFees.setFee(4.0);
        when(rbfFeeRepository.findByCityIgnoreCaseAndVehicleIgnoreCase(anyString(), anyString()))
                .thenReturn(Optional.of(rbfFees));

        double totalFee = deliveryService.getTotalFee("Tartu", "Bike", LocalDateTime.now());
        assertEquals(4.0, totalFee);
    }

    @Test
    void highest_fee_test() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        when(stationRepository.findFirstByTimestampOrderByTimestampDesc(any(LocalDateTime.class))).thenReturn(Optional.of(createStation("Tallinn", 123, -20.0, 15.0, "sleet", now)));
        when(stationRepository.findFirstByNameOrderByTimestampDesc(anyString())).thenReturn(Optional.of(createStation("Tallinn", 123, -20.0, 15.0, "sleet", now)));

        RBFFees rbfFees = new RBFFees();
        rbfFees.setCity("Tallinn");
        rbfFees.setVehicle("Car");
        rbfFees.setFee(6.0);
        when(rbfFeeRepository.findByCityIgnoreCaseAndVehicleIgnoreCase(anyString(), anyString()))
                .thenReturn(Optional.of(rbfFees));

        double totalFee = deliveryService.getTotalFee("Tallinn", "Car", LocalDateTime.now());
        assertEquals(6.0, totalFee);
    }

    @Test
    void lowest_fee_test() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        when(stationRepository.findFirstByTimestampOrderByTimestampDesc(any(LocalDateTime.class))).thenReturn(Optional.of(createStation("Pärnu", 123, 5.0, 5.0, "rain", now)));
        when(stationRepository.findFirstByNameOrderByTimestampDesc(anyString())).thenReturn(Optional.of(createStation("Pärnu", 123, 5.0, 5.0, "rain", now)));

        RBFFees rbfFees = new RBFFees();
        rbfFees.setCity("Pärnu");
        rbfFees.setVehicle("Scooter");
        rbfFees.setFee(2.5);
        when(rbfFeeRepository.findByCityIgnoreCaseAndVehicleIgnoreCase(anyString(), anyString()))
                .thenReturn(Optional.of(rbfFees));

        double totalFee = deliveryService.getTotalFee("Pärnu", "Scooter", LocalDateTime.now());
        assertEquals(2.5, totalFee);
    }

    @Test
    void usage_forbidden_weather_fee() {
        LocalDateTime now = LocalDateTime.now();
        when(stationRepository.findFirstByTimestampOrderByTimestampDesc(any(LocalDateTime.class))).thenReturn(Optional.of(createStation("Pärnu", 123, 5.0, 5.0, "hail", now)));
        when(stationRepository.findFirstByNameOrderByTimestampDesc(anyString())).thenReturn(Optional.of(createStation("Pärnu", 123, 5.0, 5.0, "hail", now)));

        assertThrows(IOException.class, () -> deliveryService.getTotalFee("Pärnu", "Scooter", LocalDateTime.now()));
    }

    @Test
    void usage_forbidden_wind_fee() throws IOException {
        LocalDateTime now = LocalDateTime.now();
        when(stationRepository.findFirstByTimestampOrderByTimestampDesc(any(LocalDateTime.class))).thenReturn(Optional.of(createStation("Pärnu", 123, 5.0, 100.0, "sunny", now)));
        when(stationRepository.findFirstByNameOrderByTimestampDesc(anyString())).thenReturn(Optional.of(createStation("Pärnu", 123, 5.0, 100.0, "sunny", now)));

        assertThrows(IOException.class, () -> deliveryService.getTotalFee("Pärnu", "Bike", null));
    }

    @Test
    void getTotalFee_CalculatesCorrectly() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        when(stationRepository.findFirstByTimestampOrderByTimestampDesc(any(LocalDateTime.class))).thenReturn(Optional.of(createStation("Tallinn", 123, -15.0, 15.0, "sunny", now)));
        when(stationRepository.findFirstByNameOrderByTimestampDesc(anyString())).thenReturn(Optional.of(createStation("Tallinn", 123, -15.0, 15.0, "sunny", now)));
        RBFFees rbfFees = new RBFFees();
        rbfFees.setCity("Tallinn");
        rbfFees.setVehicle("Bike");
        rbfFees.setFee(3.0);
        when(rbfFeeRepository.findByCityIgnoreCaseAndVehicleIgnoreCase(anyString(), anyString()))
                .thenReturn(Optional.of(rbfFees));

        ATEFFees atefFees = new ATEFFees();
        atefFees.setTemperature(-10.0);
        atefFees.setVehicle("Bike");
        atefFees.setFee(2.0);
        when(atefFeeRepository.findByVehicleIgnoreCaseAndTemperature(anyString(), anyDouble()))
                .thenReturn(Optional.of(atefFees));

        WSEFFees wsefFees = new WSEFFees();
        wsefFees.setSpeed(15.0);
        wsefFees.setFee(0.5);
        when(wsefFeeRepository.findBySpeed(anyDouble()))
                .thenReturn(Optional.of(wsefFees));

        WPEFFees wpefFees = new WPEFFees();
        wpefFees.setVehicle("Bike");
        wpefFees.setWeather("sunny");
        wpefFees.setFee(3.0);
        when(wpefFeeRepository.findByVehicleIgnoreCaseAndWeatherIgnoreCase(anyString(), anyString()))
                .thenReturn(Optional.of(wpefFees));

        double totalFee = deliveryService.getTotalFee("Tallinn", "Bike", LocalDateTime.now());
        assertEquals(8.5, totalFee);
    }

    @Test
    void updatingDatabase_SavesCorrectly() {
        LocalDateTime timestamp = LocalDateTime.now();
        deliveryService.updatingDatabase("Tallinn", 123, -15.0, 15.0, "sunny", timestamp);

        ArgumentCaptor<Station> stationCaptor = ArgumentCaptor.forClass(Station.class);
        verify(stationRepository).save(stationCaptor.capture());

        Station savedStation = stationCaptor.getValue();
        assertEquals("Tallinn", savedStation.getName());
        assertEquals(123, savedStation.getWMO());
        assertEquals(-15.0, savedStation.getTemperature());
        assertEquals(15.0, savedStation.getWindSpeed());
        assertEquals("sunny", savedStation.getWeatherPhenomenon());
        assertEquals(timestamp, savedStation.getTimestamp());
    }

    @Test
    void getRBFFee() throws Exception {
        RBFFees rbfFees = new RBFFees();
        rbfFees.setCity("Tallinn");
        rbfFees.setVehicle("Bike");
        rbfFees.setFee(3.0);
        when(rbfFeeRepository.findByCityIgnoreCaseAndVehicleIgnoreCase(anyString(), anyString()))
                .thenReturn(Optional.of(rbfFees));

        double totalFee = deliveryService.getRBF("Tallinn", "Bike");
        assertEquals(3.0, totalFee);
    }

    @Test
    void getATEF() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        when(stationRepository.findFirstByTimestampOrderByTimestampDesc(any(LocalDateTime.class))).thenReturn(Optional.of(createStation("Tallinn", 123, -15.0, 15.0, "sunny", now)));
        when(stationRepository.findFirstByNameOrderByTimestampDesc(anyString())).thenReturn(Optional.of(createStation("Tallinn", 123, -15.0, 15.0, "sunny", now)));
        ATEFFees atefFees = new ATEFFees();
        atefFees.setTemperature(-10.0);
        atefFees.setVehicle("Bike");
        atefFees.setFee(3.0);
        when(atefFeeRepository.findByVehicleIgnoreCaseAndTemperature(anyString(), anyDouble()))
                .thenReturn(Optional.of(atefFees));

        double totalFee = deliveryService.getATEF("Tallinn", "Bike", null);
        assertEquals(3.0, totalFee);
    }

    @Test
    void getWSEF() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        when(stationRepository.findFirstByTimestampOrderByTimestampDesc(any(LocalDateTime.class))).thenReturn(Optional.of(createStation("Tallinn", 123, -15.0, 15.0, "sunny", now)));
        when(stationRepository.findFirstByNameOrderByTimestampDesc(anyString())).thenReturn(Optional.of(createStation("Tallinn", 123, -15.0, 15.0, "sunny", now)));

        WSEFFees wsefFees = new WSEFFees();
        wsefFees.setSpeed(15.0);
        wsefFees.setFee(0.5);
        when(wsefFeeRepository.findBySpeed(anyDouble())).thenReturn(Optional.of(wsefFees));

        Double totalFee = deliveryService.getWSEF("Tallinn", "Bike", null);
        assertEquals(0.5, totalFee);
    }

    @Test
    void getWPEF() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        when(stationRepository.findFirstByTimestampOrderByTimestampDesc(any(LocalDateTime.class))).thenReturn(Optional.of(createStation("Tallinn", 123, -15.0, 15.0, "sunny", now)));
        when(stationRepository.findFirstByNameOrderByTimestampDesc(anyString())).thenReturn(Optional.of(createStation("Tallinn", 123, -15.0, 15.0, "sunny", now)));

        WPEFFees wpefFees = new WPEFFees();
        wpefFees.setVehicle("Bike");
        wpefFees.setWeather("sunny");
        wpefFees.setFee(3.0);
        when(wpefFeeRepository.findByVehicleIgnoreCaseAndWeatherIgnoreCase(anyString(), anyString())).thenReturn(Optional.of(wpefFees));

        Double totalFee = deliveryService.getWPEF("Tallinn", "Bike", null);
        assertEquals(3.0, totalFee);
    }

    @Test
    void getWPEF_ThrowsException_WhenWeatherPhenomenonForbidden() {
        when(stationRepository.findFirstByNameOrderByTimestampDesc(any())).thenReturn(Optional.of(new Station()));

        assertThrows(IOException.class, () -> deliveryService.getWPEF("Tallinn", "Bike", null));
    }

    private Station createStation(String name, Integer wmo, Double tempertaure, Double windSpeed, String weatherPhenomenon, LocalDateTime date) {
        Station station = new Station();
        station.setName(name);
        station.setWMO(wmo);
        station.setTemperature(tempertaure);
        station.setWindSpeed(windSpeed);
        station.setWeatherPhenomenon(weatherPhenomenon);
        station.setTimestamp(date);
        return station;
    }
}
