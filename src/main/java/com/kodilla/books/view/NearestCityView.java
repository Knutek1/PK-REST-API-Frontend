package com.kodilla.books.view;

import com.kodilla.books.domain.CityDataAirVisualResponse;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Route("nearest_city")
@UIScope
@Component
public class NearestCityView extends VerticalLayout {

    private final RestTemplate restTemplate;

    @Autowired
    public NearestCityView(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    private void init() {
        try {
            String endpointUrl = "http://localhost:8080/v1/Poland/nearest_city";
            CityDataAirVisualResponse cityDataAirVisualResponse = restTemplate.getForObject(endpointUrl, CityDataAirVisualResponse.class);

            add(new H3("Location Information"));
            add(new Span("City: " + cityDataAirVisualResponse.getData().getCity()));
            add(new Span("State: " + cityDataAirVisualResponse.getData().getState()));
            add(new Span("Country: " + cityDataAirVisualResponse.getData().getCountry()));

            add(new H3("Current Pollution"));
            CityDataAirVisualResponse.Pollution pollution = cityDataAirVisualResponse.getData().getCurrent().getPollution();
            add(new Span("Timestamp: " + pollution.getTs()));
            add(new Span("AQI (US): " + pollution.getAqius()));
            add(new Span("Main Pollutant: " + pollution.getMainus()));

            add(new H3("Current Weather"));
            CityDataAirVisualResponse.Weather weather = cityDataAirVisualResponse.getData().getCurrent().getWeather();
            add(new Span("Timestamp: " + weather.getTs()));
            add(new Span("Temperature: " + weather.getTp() + " °C"));
            add(new Span("Pressure: " + weather.getPr() + " hPa"));
            add(new Span("Humidity: " + weather.getHu() + " %"));
            add(new Span("Wind Speed: " + weather.getWs() + " m/s"));
            add(new Span("Wind Direction: " + weather.getWd() + " °"));
            add(new Span("Icon: " + weather.getIc()));
        } catch (HttpClientErrorException e) {
            System.err.println("Error while fetching data from the server: " + e.getMessage());
        }
    }
}


