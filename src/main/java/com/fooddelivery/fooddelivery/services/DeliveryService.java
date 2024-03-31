package com.fooddelivery.fooddelivery.services;

import com.fooddelivery.fooddelivery.entities.*;
import com.fooddelivery.fooddelivery.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing delivery operations.
 */
@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final StationRepository stationRepository;
    private final RBFFeeRepository rbfFeeRepository;
    private final ATEFFeeRepository atefFeeRepository;
    private final WSEFFeeRepository wsefFeeRepository;
    private final WPEFFeeRepository wpefFeeRepository;
    private static final String TALLINN_NAME = "Tallinn-Harku";
    private static final String TALLINN_CITY = "Tallinn";
    private static final String TARTU_NAME = "Tartu-Tõravere";
    private static final String TARTU_CITY = "Tartu";
    private static final String PARNU_CITY = "Pärnu";
    private static final String PARNU_NAME = "Pärnu";
    private static final String BIKE = "Bike";
    private static final String SCOOTER = "Scooter";
    private static final double NO_FEE = 0.0;
    private static final double TEMPERATURE_MINUS_TEN = -10.0;
    private static final double TEMPERATURE_ZERO = 0.0;
    private static final double WIND_SPEED_10 = 10.0;
    private static final double WIND_SPEED_20 = 20.0;
    private static final String WEATHER_PHENOMENON_GLAZE = "glaze";
    private static final String WEATHER_PHENOMENON_HAIL = "hail";
    private static final String WEATHER_PHENOMENON_THUNDER = "thunder";
    private static final String ERROR_WARNING = "No such city or vehicle providing this service.";

    /**
     * Updates the database with the provided station information from WeatherData.
     *
     * @param name            The name of the station.
     * @param WMO             The World Meteorological Organization code.
     * @param temperature     The temperature at the station.
     * @param windSpeed       The wind speed at the station.
     * @param weatherPhenomenon The weather phenomenon at the station.
     * @param timestamp       The timestamp of the data.
     */
    public void updatingDatabase(String name, Integer WMO, Double temperature, Double windSpeed, String weatherPhenomenon, LocalDateTime timestamp) {
        Station station = new Station();
        station.setName(name);
        station.setWMO(WMO);
        station.setTemperature(temperature);
        station.setWindSpeed(windSpeed);
        station.setWeatherPhenomenon(weatherPhenomenon);
        station.setTimestamp(timestamp);
        stationRepository.save(station);
    }
    /**
     * Calculates the total fee for delivery in the specified city using the given vehicle at the given time.
     *
     * @param city     The city for delivery.
     * @param vehicle  The vehicle used for delivery.
     * @param dateTime The date and time of the delivery.
     * @return The total fee for delivery.
     * @throws IOException If wrong city/vehicle/date that is not providing service is entered.
     */
    public double getTotalFee(String city, String vehicle, LocalDateTime dateTime) throws IOException {
        return getRBF(city, vehicle) + getATEF(city, vehicle, dateTime) + getWSEF(city, vehicle, dateTime) + getWPEF(city, vehicle,dateTime);
    }
    /**
     * Retrieves the regional base fees (RBF) for delivery in the specified city using the given vehicle.
     *
     * @param city    The city for delivery.
     * @param vehicle The vehicle used for delivery.
     * @return RBF.
     */
    public Double getRBF(String city, String vehicle) {
        Optional<RBFFees> feeOptional = rbfFeeRepository.findByCityIgnoreCaseAndVehicleIgnoreCase(city.toLowerCase(), vehicle.toLowerCase());
        return feeOptional.map(RBFFees::getFee).orElse(NO_FEE);
    }
    /**
     * Retrieves the air temperature fees (ATE) for delivery in the specified city using the given vehicle at the given time.
     *
     * @param city     The city for delivery.
     * @param vehicle  The vehicle used for delivery.
     * @param dateTime The date and time of the delivery.
     * @return ATEF.
     */
    public Double getATEF(String city, String vehicle, LocalDateTime dateTime) throws IOException {
        String name = getStationName(city);
        try {
            Optional<Station> stationShell;
            if (dateTime != null) {
                stationShell = stationRepository.findFirstByTimestampOrderByTimestampDesc(dateTime);
            } else {
                stationShell = stationRepository.findFirstByNameOrderByTimestampDesc(name);
            }
            if (stationShell.isEmpty()){
                throw new NullPointerException(ERROR_WARNING);
            }
            Station station = stationShell.get();
            Double temperature = station.getTemperature();
            if (vehicle.equalsIgnoreCase(SCOOTER) || vehicle.equalsIgnoreCase(BIKE)) {
                if (temperature < TEMPERATURE_MINUS_TEN) {
                    Optional<ATEFFees> feeOptional = atefFeeRepository.findByVehicleIgnoreCaseAndTemperature(vehicle, TEMPERATURE_MINUS_TEN);
                    return feeOptional.map(ATEFFees::getFee).orElse(NO_FEE);
                }
                if (temperature > TEMPERATURE_MINUS_TEN && temperature < TEMPERATURE_ZERO) {
                    Optional<ATEFFees> feeOptional = atefFeeRepository.findByVehicleIgnoreCaseAndTemperature(vehicle, TEMPERATURE_ZERO);
                    return feeOptional.map(ATEFFees::getFee).orElse(NO_FEE);
                }
            }
            return NO_FEE;
        } catch (NullPointerException | IllegalArgumentException | DataAccessException e) {
            throw new IOException("Can not get ATEF.", e);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the wind speed fees (WSE) for delivery in the specified city using the given vehicle at the given time.
     *
     * @param city     The city for delivery.
     * @param vehicle  The vehicle used for delivery.
     * @param dateTime The date and time of the delivery.
     * @return WSEF.
     */
    public Double getWSEF(String city, String vehicle, LocalDateTime dateTime) throws IOException {
        String name = getStationName(city);
        try {
            Optional<Station> stationShell;
            if (dateTime != null) {
                stationShell = stationRepository.findFirstByTimestampOrderByTimestampDesc(dateTime);
            } else {
                stationShell = stationRepository.findFirstByNameOrderByTimestampDesc(name);
            }
            if (stationShell.isEmpty()){
                throw new NullPointerException(ERROR_WARNING);
            }
            Station station = stationShell.get();
            Double speed = station.getWindSpeed();
            if (vehicle.equalsIgnoreCase(BIKE)) {
                if (speed > WIND_SPEED_10 && speed < WIND_SPEED_20) {
                    Optional<WSEFFees> feeOptional = wsefFeeRepository.findBySpeed(WIND_SPEED_10);
                    return feeOptional.map(WSEFFees::getFee).orElse(NO_FEE);
                }
                if (speed > WIND_SPEED_20) {
                    throw new IOException("Usage of selected vehicle type is forbidden");
                }
            }
            return NO_FEE;
        } catch (NullPointerException | IllegalArgumentException | DataAccessException e) {
            throw new IOException("Can not get WSEF.", e);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the weather phenomenon fees (WPE) for delivery in the specified city using the given vehicle at the given time.
     *
     * @param city     The city for delivery.
     * @param vehicle  The vehicle used for delivery.
     * @param dateTime The date and time of the delivery.
     * @return WPE.
     */
    public Double getWPEF(String city, String vehicle, LocalDateTime dateTime) throws IOException {
        String name = getStationName(city);
        try {
            Optional<Station> stationShell;
            if (dateTime != null) {
                stationShell = stationRepository.findFirstByTimestampOrderByTimestampDesc(dateTime);
                if (stationShell.isEmpty()) {
                    throw new NullPointerException(ERROR_WARNING);
                }
            } else {
                stationShell = stationRepository.findFirstByNameOrderByTimestampDesc(name);
                if (stationShell.isEmpty()) {
                    throw new NullPointerException(ERROR_WARNING);
                }
            }
            Station station = stationShell.get();
            List<String> weather = Arrays.asList(station.getWeatherPhenomenon().split(" ", -1));
            for (String element: weather) {
                if (element.equalsIgnoreCase(WEATHER_PHENOMENON_GLAZE) || element.equalsIgnoreCase(WEATHER_PHENOMENON_HAIL) || element.equalsIgnoreCase(WEATHER_PHENOMENON_THUNDER)) {
                    throw new IOException("Usage of selected vehicle type is forbidden");
                }
                Optional<WPEFFees> feeOptional = wpefFeeRepository.findByVehicleIgnoreCaseAndWeatherIgnoreCase(vehicle, element.toLowerCase());
                if (feeOptional.isEmpty()) {
                    continue;
                } else {
                    return feeOptional.map(WPEFFees::getFee).orElse(NO_FEE);
                }
            }

            return NO_FEE;
        } catch (NullPointerException e) {
            throw new IOException("Can not get WPEF.", e);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private String getStationName(String city) {
        if (city.equalsIgnoreCase(TALLINN_CITY)) return TALLINN_NAME;
        if (city.equalsIgnoreCase(TARTU_CITY)) return TARTU_NAME;
        if (city.equalsIgnoreCase(PARNU_CITY)) return PARNU_NAME;
        throw new RuntimeException("No such city provides this service.");
    }
}