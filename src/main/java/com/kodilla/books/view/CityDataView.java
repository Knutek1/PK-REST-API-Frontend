package com.kodilla.books.view;

import com.kodilla.books.domain.response.*;
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

    private ComboBox<StatesAirVisualResponse.State> stateComboBox;
    private ComboBox<CitiesAirVisualResponse.City> cityComboBox;
    private ComboBox<BasesAirTableResponse.Base> baseCombobox;
    private ComboBox<TablesAirTableResponse.Table> tableCombobox;
    private Button cityButton;
    private Button airTableButton;
    private Button baseButton;
    private Button tableButton;

    @Autowired
    public CityDataView(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.cityDataContainer = new VerticalLayout();
        this.airTableService = new AirTableService(restTemplate);

        createComponents();
        addComponentsToLayout();
        initializeComponentVisibility();
    }

    private void createComponents() {
        stateComboBox = new ComboBox<>("Select State");
        stateComboBox.setItems(getStates());

        cityComboBox = new ComboBox<>("Select City");
        cityButton = new Button("Confirm City");
        airTableButton = new Button("Post to AirTable");
        baseCombobox = new ComboBox<>("Select base");
        baseButton = new Button("Confirm base");
        tableCombobox = new ComboBox<>("Select table");
        tableButton = new Button("Confirm table");

        cityButton.addClickListener(event -> cityButtonClick());
        airTableButton.addClickListener(event -> airTableService.airTableButtonClick(baseCombobox, baseButton, airTableButton));
        baseButton.addClickListener(event -> airTableService.baseButtonClick(baseCombobox, tableCombobox, tableButton));
        tableButton.addClickListener(event -> airTableService.tableButtonClick(baseCombobox, tableCombobox, cityDataAirVisualResponse, baseButton, tableButton));
    }

    private void addComponentsToLayout() {
        VerticalLayout inputLayout = new VerticalLayout(stateComboBox, new Button("Confirm State", event -> stateButtonClick()), cityComboBox, cityButton);
        VerticalLayout airtableLayout = new VerticalLayout(baseCombobox, baseButton, tableCombobox, tableButton, airTableButton);
        HorizontalLayout mainLayout = new HorizontalLayout(inputLayout, cityDataContainer, airtableLayout);
        add(mainLayout);
    }

    private void initializeComponentVisibility() {
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
