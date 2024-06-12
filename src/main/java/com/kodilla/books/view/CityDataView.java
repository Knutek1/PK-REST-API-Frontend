package com.kodilla.books.view;

import com.kodilla.books.domain.CitiesAirVisualResponse;
import com.kodilla.books.domain.CityDataAirVisualResponse;
import com.kodilla.books.domain.StatesAirVisualResponse;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Route("city_data")
@UIScope
@Component
public class CityDataView extends VerticalLayout {

    private final RestTemplate restTemplate;
    private final VerticalLayout cityDataContainer;

    @Autowired
    public CityDataView(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

        ComboBox<StatesAirVisualResponse.State> stateComboBox = new ComboBox<>("Select State");
        stateComboBox.setItems(getStates());

        ComboBox<CitiesAirVisualResponse.City> cityComboBox = new ComboBox<>("Select City");

        Button stateButton = new Button("State Request", event -> {
            StatesAirVisualResponse.State selectedState = stateComboBox.getValue();
            if (selectedState != null) {
                CitiesAirVisualResponse citiesAirVisualResponse = restTemplate.getForObject("http://localhost:8080/v1/Poland/cities?state=" + selectedState, CitiesAirVisualResponse.class);
                if (citiesAirVisualResponse != null) {
                    cityComboBox.setItems(Arrays.asList(citiesAirVisualResponse.getData()));
                    add(cityComboBox);
                    add(new Button("City Request", event1 -> {
                        CitiesAirVisualResponse.City selectedCity = cityComboBox.getValue();
                        if (selectedCity != null) {
                            CityDataAirVisualResponse cityDataAirVisualResponse = restTemplate.getForObject("http://localhost:8080/v1/Poland/city_data?state=" + selectedState + "&city=" + selectedCity, CityDataAirVisualResponse.class);
                            if (cityDataAirVisualResponse != null) {
                                displayCityData(cityDataAirVisualResponse);
                            }
                        }
                    }));
                }
            }
        });
        VerticalLayout inputLayout = new VerticalLayout(stateComboBox, stateButton);
        cityDataContainer = new VerticalLayout();

        HorizontalLayout mainLayout = new HorizontalLayout(inputLayout, cityDataContainer);
        add(mainLayout);
    }

    private void displayCityData(CityDataAirVisualResponse cityDataAirVisualResponse) {
        cityDataContainer.removeAll();
        cityDataContainer.add(new H3("Location Information"));
        cityDataContainer.add(new Span("City: " + cityDataAirVisualResponse.getData().getCity()));
        cityDataContainer.add(new Span("State: " + cityDataAirVisualResponse.getData().getState()));
        cityDataContainer.add(new Span("Country: " + cityDataAirVisualResponse.getData().getCountry()));

        cityDataContainer.add(new H3("Current Pollution"));
        CityDataAirVisualResponse.Pollution pollution = cityDataAirVisualResponse.getData().getCurrent().getPollution();
        cityDataContainer.add(new Span("Timestamp: " + pollution.getTs()));
        cityDataContainer.add(new Span("AQI (US): " + pollution.getAqius()));
        cityDataContainer.add(new Span("Main Pollutant: " + pollution.getMainus()));

        cityDataContainer.add(new H3("Current Weather"));
        CityDataAirVisualResponse.Weather weather = cityDataAirVisualResponse.getData().getCurrent().getWeather();
        cityDataContainer.add(new Span("Timestamp: " + weather.getTs()));
        cityDataContainer.add(new Span("Temperature: " + weather.getTp() + " °C"));
        cityDataContainer.add(new Span("Pressure: " + weather.getPr() + " hPa"));
        cityDataContainer.add(new Span("Humidity: " + weather.getHu() + " %"));
        cityDataContainer.add(new Span("Wind Speed: " + weather.getWs() + " m/s"));
        cityDataContainer.add(new Span("Wind Direction: " + weather.getWd() + " °"));
        cityDataContainer.add(new Span("Icon: " + weather.getIc()));
    }

    private List<StatesAirVisualResponse.State> getStates() {
        String endpointUrl = "http://localhost:8080/v1/Poland/states";
        StatesAirVisualResponse statesAirVisualResponse = restTemplate.getForObject(endpointUrl, StatesAirVisualResponse.class);
        assert statesAirVisualResponse != null;
        return Arrays.asList(statesAirVisualResponse.getData());
    }
}
