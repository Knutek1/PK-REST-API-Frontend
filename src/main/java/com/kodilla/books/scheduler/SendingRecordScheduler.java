package com.kodilla.books.scheduler;

import com.kodilla.books.domain.response.airVisual.CityDataAirVisualResponse;
import com.kodilla.books.service.AirTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SendingRecordScheduler {
    private final RestTemplate restTemplate;
    private final AirTableService airTableService;

    @Autowired
    public SendingRecordScheduler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.airTableService = new AirTableService(restTemplate);
    }

    @Scheduled(fixedRate = 600000)
    public void performScheduledTasks() {
        String endpointUrl = "http://localhost:8080/v1/Poland/nearest_city";
        CityDataAirVisualResponse cityDataAirVisualResponse = restTemplate.getForObject(endpointUrl, CityDataAirVisualResponse.class);
        if (cityDataAirVisualResponse != null) {
            airTableService.scheduledRecordSending(cityDataAirVisualResponse);
        }
    }
}
