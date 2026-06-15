package com.planehangar.hangar.client;

import com.planehangar.hangar.dto.AircraftDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/*
 * Talks to the Aircraft Service over its REST API*/
@Component
public class AircraftClient {

    private final RestTemplate restTemplate;
    private final String aircraftServiceUrl;

    public AircraftClient(RestTemplate restTemplate,
                           @Value("${aircraft.service.url:http://localhost:8081}") String aircraftServiceUrl) {
        this.restTemplate = restTemplate;
        this.aircraftServiceUrl = aircraftServiceUrl;
    }

    /**
     * Fetches an aircraft by ID from the Aircraft Service.
     *
     * @param aircraftId the ID of the aircraft to look up
     * @return the aircraft if found, or empty if it does not exist or the
     *         Aircraft Service is unreachable
     */
    public Optional<AircraftDto> getAircraft(Long aircraftId) {
        String url = aircraftServiceUrl + "/aircraft/" + aircraftId;
        try {
            AircraftDto aircraft = restTemplate.getForObject(url, AircraftDto.class);
            return Optional.ofNullable(aircraft);
        } catch (HttpClientErrorException.NotFound notFound) {
            return Optional.empty();
        }
    }
}
