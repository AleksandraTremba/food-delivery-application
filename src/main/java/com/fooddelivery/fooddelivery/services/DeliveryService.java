package com.fooddelivery.fooddelivery.services;

import com.fooddelivery.fooddelivery.entities.Station;
import com.fooddelivery.fooddelivery.repository.StationReposirory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final StationReposirory stationReposirory;
    public void updatingDatabase() {
        //this is just an example for the time being
        Station station = new Station();
        station.setWMO(123);
        station.setTemperature(17);
        station.setWindSpeed(125);
        station.setWeatherPhenomenon("Rain");
        station.setTimestamp(currentDate());
        stationReposirory.save(station);
    }

    public LocalDateTime currentDate() {
        return LocalDateTime.now();
    }
}
