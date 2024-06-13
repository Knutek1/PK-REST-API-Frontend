package com.kodilla.books.view;

import com.kodilla.books.domain.CitiesAirVisualResponse;
import com.kodilla.books.domain.CityDataAirVisualResponse;
import com.kodilla.books.domain.StatesAirVisualResponse;
import com.kodilla.books.service.CityDataDisplayer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
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
public class CityDataView extends HorizontalLayout {

    private final RestTemplate restTemplate;
    private final VerticalLayout cityDataContainer;

    @Autowired
    public CityDataView(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        cityDataContainer = new VerticalLayout();

        ComboBox<StatesAirVisualResponse.State> stateComboBox = new ComboBox<>("Select State");
        stateComboBox.setItems(getStates());
        Button stateButton;
        ComboBox<CitiesAirVisualResponse.City> cityComboBox = new ComboBox<>("Select City");
        Button cityButton = new Button("City Request");

        cityComboBox.setVisible(false);
        cityButton.setVisible(false);

        stateButton = new Button("State Request", event -> {
            StatesAirVisualResponse.State selectedState = stateComboBox.getValue();
            if (selectedState != null) {
                CitiesAirVisualResponse citiesAirVisualResponse = restTemplate.getForObject("http://localhost:8080/v1/Poland/cities?state=" + selectedState, CitiesAirVisualResponse.class);
                if (citiesAirVisualResponse != null) {
                    cityComboBox.setItems(Arrays.asList(citiesAirVisualResponse.getData()));
                    cityComboBox.setVisible(true);
                    cityButton.setVisible(true);
                }
            }
        });

        cityButton.addClickListener(event -> {
            StatesAirVisualResponse.State selectedState = stateComboBox.getValue();
            CitiesAirVisualResponse.City selectedCity = cityComboBox.getValue();
            if (selectedCity != null) {
                CityDataAirVisualResponse cityDataAirVisualResponse = restTemplate.getForObject("http://localhost:8080/v1/Poland/city_data?state=" + selectedState + "&city=" + selectedCity, CityDataAirVisualResponse.class);
                if (cityDataAirVisualResponse != null) {
                    CityDataDisplayer.displayCityData(cityDataContainer, cityDataAirVisualResponse);
                }
            }
        });

        VerticalLayout inputLayout = new VerticalLayout(stateComboBox, stateButton, cityComboBox, cityButton);
        HorizontalLayout mainLayout = new HorizontalLayout(inputLayout, cityDataContainer);
        add(mainLayout);
    }

    private List<StatesAirVisualResponse.State> getStates() {
        String endpointUrl = "http://localhost:8080/v1/Poland/states";
        StatesAirVisualResponse statesAirVisualResponse = restTemplate.getForObject(endpointUrl, StatesAirVisualResponse.class);
        assert statesAirVisualResponse != null;
        return Arrays.asList(statesAirVisualResponse.getData());
    }
}
