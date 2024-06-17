package com.kodilla.books.service;

import com.kodilla.books.domain.request.AddRecordsAirTableRequest;
import com.kodilla.books.domain.response.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class AirTableService {

    private final RestTemplate restTemplate;

    public AirTableService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void airTableButtonClick(ComboBox<BasesAirTableResponse.Base> baseCombobox, Button baseButton, Button airTableButton) {
        BasesAirTableResponse basesAirTableResponse = restTemplate.getForObject("http://localhost:8080/v1/bases", BasesAirTableResponse.class);
        if (basesAirTableResponse != null) {
            baseCombobox.setItems(Arrays.asList(basesAirTableResponse.getBases()));
            baseCombobox.setVisible(true);
            baseButton.setVisible(true);
            airTableButton.setVisible(false);
        }
    }

    public void baseButtonClick(ComboBox<BasesAirTableResponse.Base> baseCombobox, ComboBox<TablesAirTableResponse.Table> tableCombobox, Button tableButton) {
        BasesAirTableResponse.Base selectedBase = baseCombobox.getValue();
        if (selectedBase != null) {
            TablesAirTableResponse tablesAirTableResponse = restTemplate.getForObject("http://localhost:8080/v1/tables/" + selectedBase.getId(), TablesAirTableResponse.class);
            if (tablesAirTableResponse != null) {
                tableCombobox.setItems(Arrays.asList(tablesAirTableResponse.getTables()));
                tableCombobox.setVisible(true);
                tableButton.setVisible(true);
            }
        }
    }

    public void tableButtonClick(ComboBox<BasesAirTableResponse.Base> baseCombobox, ComboBox<TablesAirTableResponse.Table> tableCombobox, CityDataAirVisualResponse cityDataAirVisualResponse, Button baseButton, Button tableButton) {
        BasesAirTableResponse.Base selectedBase = baseCombobox.getValue();
        TablesAirTableResponse.Table selectedTable = tableCombobox.getValue();
        if (selectedTable != null) {
            AddRecordsAirTableRequest.Fields fields = new AddRecordsAirTableRequest.Fields(
                    cityDataAirVisualResponse.getData().getCurrent().getPollution().getAqius(),
                    cityDataAirVisualResponse.getData().getCity(),
                    cityDataAirVisualResponse.getData().getCurrent().getWeather().getTp());

            AddRecordsAirTableRequest.Record record = new AddRecordsAirTableRequest.Record(fields);
            AddRecordsAirTableRequest request = new AddRecordsAirTableRequest(List.of(record));

            restTemplate.postForObject("http://localhost:8080/v1/records/" + selectedBase.getId() + "/" + selectedTable.getId(), request, RecordsAirTableResponse.class);
            Notification.show("Wysłano dane temperatury i AQIUS do Airtable");
            baseButton.setVisible(false);
            tableButton.setVisible(false);
            baseCombobox.setVisible(false);
            tableCombobox.setVisible(false);
        }
    }
}

