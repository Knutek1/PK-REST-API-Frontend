package com.kodilla.books.service;

import com.kodilla.books.domain.CityDataAirVisualResponse;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class CityDataDisplayer {

    public static void displayCityData(VerticalLayout container, CityDataAirVisualResponse cityDataAirVisualResponse) {
        container.removeAll();
        container.add(new H3("Location Information"));
        container.add(new Span("City: " + cityDataAirVisualResponse.getData().getCity()));
        container.add(new Span("State: " + cityDataAirVisualResponse.getData().getState()));
        container.add(new Span("Country: " + cityDataAirVisualResponse.getData().getCountry()));

        container.add(new H3("Current Pollution"));
        CityDataAirVisualResponse.Pollution pollution = cityDataAirVisualResponse.getData().getCurrent().getPollution();
        container.add(new Span("Timestamp: " + pollution.getTs()));
        container.add(new Span("AQI (US): " + pollution.getAqius()));
        container.add(new Span("Main Pollutant: " + pollution.getMainus()));

        container.add(new H3("Current Weather"));
        CityDataAirVisualResponse.Weather weather = cityDataAirVisualResponse.getData().getCurrent().getWeather();
        container.add(new Span("Timestamp: " + weather.getTs()));
        container.add(new Span("Temperature: " + weather.getTp() + " °C"));
        container.add(new Span("Pressure: " + weather.getPr() + " hPa"));
        container.add(new Span("Humidity: " + weather.getHu() + " %"));
        container.add(new Span("Wind Speed: " + weather.getWs() + " m/s"));
        container.add(new Span("Wind Direction: " + weather.getWd() + " °"));
        container.add(new Span("Icon: " + weather.getIc()));
    }
}
