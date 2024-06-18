package com.kodilla.books.view.airTable;

import com.kodilla.books.domain.response.airTable.BasesAirTableResponse;
import com.kodilla.books.domain.response.airTable.RecordsAirTableResponse;
import com.kodilla.books.domain.response.airTable.TablesAirTableResponse;
import com.kodilla.books.service.AirTableService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Route("records")
public class RecordListView extends VerticalLayout {

    private final Grid<RecordsAirTableResponse.Record> grid;
    private final RestTemplate restTemplate;
    private final AirTableService airTableService;
    private final ComboBox<BasesAirTableResponse.Base> baseCombobox = new ComboBox<>("Select base");
    private final ComboBox<TablesAirTableResponse.Table> tableCombobox = new ComboBox<>("Select table");
    private final Button baseButton = new Button("Confirm base");
    private final Button tableButton = new Button("Confirm table");
    private final TextField cityFilter = new TextField();
    private final ComboBox<RecordsAirTableResponse.Record> recordComboBox = new ComboBox<>("Select record Id");
    private final Button deleteRecordButton = new Button("delete record");

    public RecordListView(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.airTableService = new AirTableService(restTemplate);
        this.grid = new Grid<>();

        initialComponentsSettings();
        buttonActions();
        layout();
    }

    private void initialComponentsSettings() {
        cityFilter.setPlaceholder("Filter by city...");
        cityFilter.setClearButtonVisible(true);
        cityFilter.setValueChangeMode(ValueChangeMode.EAGER);
        recordComboBox.setVisible(false);
        deleteRecordButton.setVisible(false);
        tableCombobox.setVisible(false);
        tableButton.setVisible(false);
        cityFilter.setVisible(false);
    }

    private void buttonActions() {
        airTableService.airTableButtonClick(baseCombobox, baseButton, new Button());
        baseButton.addClickListener(event -> airTableService.baseButtonClick(baseCombobox, tableCombobox, tableButton));
        tableButton.addClickListener(event -> getRecords(baseCombobox, tableCombobox));
        cityFilter.addValueChangeListener(event -> cityFilter());
        deleteRecordButton.addClickListener(event -> deleteRecord(baseCombobox, tableCombobox, recordComboBox));
    }

    public void getRecords(ComboBox<BasesAirTableResponse.Base> baseCombobox, ComboBox<TablesAirTableResponse.Table> tableCombobox) {
        BasesAirTableResponse.Base selectedBase = baseCombobox.getValue();
        TablesAirTableResponse.Table selectedTable = tableCombobox.getValue();

        RecordsAirTableResponse recordsAirTableResponse = restTemplate.getForObject(
                "http://localhost:8080/v1/records/" + selectedBase.getId() + "/" + selectedTable.getId(),
                RecordsAirTableResponse.class);

        assert recordsAirTableResponse != null;
        List<RecordsAirTableResponse.Record> records = recordsAirTableResponse.getRecords();
        grid.setItems(records);
        recordComboBox.setItems(records);
        recordComboBox.setVisible(true);
        deleteRecordButton.setVisible(true);
        cityFilter.setVisible(true);
    }

    private void deleteRecord(ComboBox<BasesAirTableResponse.Base> baseCombobox, ComboBox<TablesAirTableResponse.Table> tableCombobox, ComboBox<RecordsAirTableResponse.Record> recordComboBox) {
        BasesAirTableResponse.Base selectedBase = baseCombobox.getValue();
        TablesAirTableResponse.Table selectedTable = tableCombobox.getValue();
        RecordsAirTableResponse.Record selectedRecord = recordComboBox.getValue();
        restTemplate.delete("http://localhost:8080/v1/records/" + selectedBase.getId() + "/" + selectedTable.getId() + "/" + selectedRecord.getId());
        getRecords(baseCombobox, tableCombobox);
    }

    private void layout() {
        grid.addColumn(record -> record.getFields().getStartDate()).setHeader("Start date").setSortable(true);
        grid.addColumn(record -> record.getFields().getMiasto()).setHeader("Miasto").setSortable(true);
        grid.addColumn(record -> record.getFields().getJakosc()).setHeader("Jakość");
        grid.addColumn(record -> record.getFields().getPomiarAQIUS()).setHeader("Pomiar AQIUS");
        grid.addColumn(record -> record.getFields().getTemperatura()).setHeader("Temperatura (°C)");
        grid.addColumn(RecordsAirTableResponse.Record::getId).setHeader("Id");
        HorizontalLayout horizontalLayout = new HorizontalLayout(baseCombobox, baseButton, tableCombobox, tableButton, recordComboBox, deleteRecordButton);
        VerticalLayout mainLayout = new VerticalLayout(horizontalLayout, cityFilter, grid);
        add(mainLayout);
    }

    private void cityFilter() {
        ListDataProvider<RecordsAirTableResponse.Record> dataProvider = (ListDataProvider<RecordsAirTableResponse.Record>) grid.getDataProvider();
        dataProvider.setFilter(record -> {
            String city = record.getFields().getMiasto();
            String cityFilterValue = cityFilter.getValue();
            return city == null || cityFilterValue == null || city.toLowerCase().contains(cityFilterValue.toLowerCase());
        });
    }
}
