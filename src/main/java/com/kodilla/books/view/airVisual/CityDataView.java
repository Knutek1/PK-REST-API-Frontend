package com.kodilla.books.view.airVisual;

import com.kodilla.books.domain.response.airTable.BasesAirTableResponse;
import com.kodilla.books.domain.response.airTable.TablesAirTableResponse;
import com.kodilla.books.domain.response.airVisual.CitiesAirVisualResponse;
import com.kodilla.books.domain.response.airVisual.CityDataAirVisualResponse;
import com.kodilla.books.domain.response.airVisual.StatesAirVisualResponse;
import com.kodilla.books.service.CityDataDisplayer;
import com.kodilla.books.service.AirTableService;
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
    private CityDataAirVisualResponse cityDataAirVisualResponse;
    private final AirTableService airTableService;
    private final ComboBox<StatesAirVisualResponse.State> stateComboBox = new ComboBox<>("Select State");
    private final ComboBox<CitiesAirVisualResponse.City> cityComboBox = new ComboBox<>("Select City");
    private final ComboBox<BasesAirTableResponse.Base> baseCombobox = new ComboBox<>("Select base");
    private final ComboBox<TablesAirTableResponse.Table> tableCombobox = new ComboBox<>("Select table");
    private final Button cityButton = new Button("Confirm City");
    private final Button airTableButton = new Button("Post to AirTable");
    private final Button baseButton = new Button("Confirm base");
    private final Button tableButton = new Button("Confirm table");

    @Autowired
    public CityDataView(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.cityDataContainer = new VerticalLayout();
        this.airTableService = new AirTableService(restTemplate);

        initialComponentsSettings();
        buttonActions();
        layout();
    }

    private void buttonActions() {
        stateComboBox.setItems(getStates());
        cityButton.addClickListener(event -> cityButtonClick());
        airTableButton.addClickListener(event -> airTableService.airTableButtonClick(baseCombobox, baseButton, airTableButton));
        baseButton.addClickListener(event -> airTableService.baseButtonClick(baseCombobox, tableCombobox, tableButton));
        tableButton.addClickListener(event -> airTableService.tableButtonClick(baseCombobox, tableCombobox, cityDataAirVisualResponse, baseButton, tableButton));
    }

    private void layout() {
        VerticalLayout inputLayout = new VerticalLayout(stateComboBox, new Button("Confirm State", event -> stateButtonClick()), cityComboBox, cityButton);
        VerticalLayout airtableLayout = new VerticalLayout(baseCombobox, baseButton, tableCombobox, tableButton, airTableButton);
        HorizontalLayout mainLayout = new HorizontalLayout(inputLayout, cityDataContainer, airtableLayout);
        add(mainLayout);
    }

    private void initialComponentsSettings() {
        cityComboBox.setVisible(false);
        cityButton.setVisible(false);
        airTableButton.setVisible(false);
        baseCombobox.setVisible(false);
        tableCombobox.setVisible(false);
        baseButton.setVisible(false);
        tableButton.setVisible(false);
    }

    private void stateButtonClick() {
        StatesAirVisualResponse.State selectedState = stateComboBox.getValue();
        if (selectedState != null) {
            CitiesAirVisualResponse citiesAirVisualResponse = restTemplate.getForObject("http://localhost:8080/v1/Poland/cities?state=" + selectedState, CitiesAirVisualResponse.class);
            if (citiesAirVisualResponse != null) {
                cityComboBox.setItems(Arrays.asList(citiesAirVisualResponse.getData()));
                cityComboBox.setVisible(true);
                cityButton.setVisible(true);
            }
        }
    }

    private void cityButtonClick() {
        StatesAirVisualResponse.State selectedState = stateComboBox.getValue();
        CitiesAirVisualResponse.City selectedCity = cityComboBox.getValue();
        if (selectedCity != null) {
            cityDataAirVisualResponse = restTemplate.getForObject("http://localhost:8080/v1/Poland/city_data?state=" + selectedState + "&city=" + selectedCity, CityDataAirVisualResponse.class);
            if (cityDataAirVisualResponse != null) {
                CityDataDisplayer.displayCityData(cityDataContainer, cityDataAirVisualResponse);
                airTableButton.setVisible(true);
            }
        }
    }

    private List<StatesAirVisualResponse.State> getStates() {
        String endpointUrl = "http://localhost:8080/v1/Poland/states";
        StatesAirVisualResponse statesAirVisualResponse = restTemplate.getForObject(endpointUrl, StatesAirVisualResponse.class);
        assert statesAirVisualResponse != null;
        return Arrays.asList(statesAirVisualResponse.getData());
    }
}
