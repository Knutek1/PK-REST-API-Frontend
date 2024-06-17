package com.kodilla.books.view;

import com.kodilla.books.domain.response.BasesAirTableResponse;
import com.kodilla.books.domain.response.CityDataAirVisualResponse;
import com.kodilla.books.domain.response.TablesAirTableResponse;
import com.kodilla.books.service.AirTableService;
import com.kodilla.books.service.CityDataDisplayer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.PostConstruct;
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

    private ComboBox<BasesAirTableResponse.Base> baseCombobox;
    private ComboBox<TablesAirTableResponse.Table> tableCombobox;
    private Button airTableButton;
    private Button baseButton;
    private Button tableButton;

    @Autowired
    public NearestCityView(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.cityDataContainer = new VerticalLayout();
        this.airTableService = new AirTableService(restTemplate);

        init();
        createComponents();
        addComponentsToLayout();
        initializeComponentVisibility();
    }

    private void createComponents() {
        airTableButton = new Button("Post to AirTable");
        baseCombobox = new ComboBox<>("Select base");
        baseButton = new Button("Confirm base");
        tableCombobox = new ComboBox<>("Select table");
        tableButton = new Button("Confirm table");


        airTableButton.addClickListener(event -> airTableService.airTableButtonClick(baseCombobox, baseButton, airTableButton));
        baseButton.addClickListener(event -> airTableService.baseButtonClick(baseCombobox, tableCombobox, tableButton));
        tableButton.addClickListener(event -> airTableService.tableButtonClick(baseCombobox, tableCombobox, cityDataAirVisualResponse, baseButton, tableButton));
    }

    private void addComponentsToLayout() {
        VerticalLayout airtableLayout = new VerticalLayout(baseCombobox, baseButton, tableCombobox, tableButton, airTableButton);
        HorizontalLayout mainLayout = new HorizontalLayout(cityDataContainer, airtableLayout);
        add(mainLayout);
    }

    private void initializeComponentVisibility() {
        baseCombobox.setVisible(false);
        tableCombobox.setVisible(false);
        baseButton.setVisible(false);
        tableButton.setVisible(false);
    }


    private void init() {


        String endpointUrl = "http://localhost:8080/v1/Poland/nearest_city";
        cityDataAirVisualResponse = restTemplate.getForObject(endpointUrl, CityDataAirVisualResponse.class);

        assert cityDataAirVisualResponse != null;
        CityDataDisplayer.displayCityData(cityDataContainer, cityDataAirVisualResponse);
        //add(cityDataContainer);
    }
}


