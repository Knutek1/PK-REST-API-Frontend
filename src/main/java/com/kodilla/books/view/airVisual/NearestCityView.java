package com.kodilla.books.view.airVisual;

import com.kodilla.books.domain.response.airTable.BasesAirTableResponse;
import com.kodilla.books.domain.response.airVisual.CityDataAirVisualResponse;
import com.kodilla.books.domain.response.airTable.TablesAirTableResponse;
import com.kodilla.books.service.AirTableService;
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

@Route("nearest_city")
@UIScope
@Component
public class NearestCityView extends VerticalLayout {

    private final RestTemplate restTemplate;
    private final AirTableService airTableService;
    private final VerticalLayout cityDataContainer;
    private CityDataAirVisualResponse cityDataAirVisualResponse;

    private final ComboBox<BasesAirTableResponse.Base> baseCombobox = new ComboBox<>("Select base");
    private final ComboBox<TablesAirTableResponse.Table> tableCombobox = new ComboBox<>("Select table");
    private final Button airTableButton = new Button("Post to AirTable");
    private final Button baseButton = new Button("Confirm base");
    private final Button tableButton = new Button("Confirm table");

    @Autowired
    public NearestCityView(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.cityDataContainer = new VerticalLayout();
        this.airTableService = new AirTableService(restTemplate);

        initialComponentsSettings();
        getNearestCityData();
        buttonActions();
        layout();
    }

    private void initialComponentsSettings() {
        baseCombobox.setVisible(false);
        tableCombobox.setVisible(false);
        baseButton.setVisible(false);
        tableButton.setVisible(false);
    }

    private void getNearestCityData() {
        String endpointUrl = "http://localhost:8080/v1/Poland/nearest_city";
        cityDataAirVisualResponse = restTemplate.getForObject(endpointUrl, CityDataAirVisualResponse.class);
        assert cityDataAirVisualResponse != null;
        CityDataDisplayer.displayCityData(cityDataContainer, cityDataAirVisualResponse);
    }

    private void buttonActions() {
        airTableButton.addClickListener(event -> airTableService.airTableButtonClick(baseCombobox, baseButton, airTableButton));
        baseButton.addClickListener(event -> airTableService.baseButtonClick(baseCombobox, tableCombobox, tableButton));
        tableButton.addClickListener(event -> airTableService.tableButtonClick(baseCombobox, tableCombobox, cityDataAirVisualResponse, baseButton, tableButton));
    }

    private void layout() {
        VerticalLayout airtableLayout = new VerticalLayout(baseCombobox, baseButton, tableCombobox, tableButton, airTableButton);
        HorizontalLayout mainLayout = new HorizontalLayout(cityDataContainer, airtableLayout);
        add(mainLayout);
    }
}


