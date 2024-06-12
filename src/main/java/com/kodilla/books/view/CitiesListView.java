package com.kodilla.books.view;

import com.kodilla.books.domain.CitiesAirVisualResponse;
import com.kodilla.books.domain.StatesAirVisualResponse;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Route("cities")
@UIScope
@Component
public class CitiesListView extends VerticalLayout {
      private final RestTemplate restTemplate;

    @Autowired
    public CitiesListView(RestTemplate restTemplate) {
            this.restTemplate = restTemplate;

        ComboBox<StatesAirVisualResponse.State> stateComboBox = new ComboBox<>("Select State");
        stateComboBox.setItems(getStates());

        Button button = new Button("Send Request", event -> {
            String selectedState = String.valueOf(stateComboBox.getValue());
            if (selectedState != null && !selectedState.isEmpty()) {
                String apiUrl = "http://localhost:8080/v1/Poland/cities";

                CitiesAirVisualResponse citiesAirVisualResponse = restTemplate.getForObject(apiUrl + "?state=" + selectedState, CitiesAirVisualResponse.class);
                assert citiesAirVisualResponse != null;
                List<CitiesAirVisualResponse.City> cities = Arrays.asList(citiesAirVisualResponse.getData());

                Grid<CitiesAirVisualResponse.City> grid = new Grid<>(CitiesAirVisualResponse.City.class);
                grid.setItems(cities);
                grid.setColumns("city");

                add(grid);

            }
        });

        add(stateComboBox, button);
    }
    public List<StatesAirVisualResponse.State> getStates() {
        String endpointUrl = "http://localhost:8080/v1/Poland/states";
        StatesAirVisualResponse statesAirVisualResponse = restTemplate.getForObject(endpointUrl, StatesAirVisualResponse.class);
        assert statesAirVisualResponse != null;
        return Arrays.asList(statesAirVisualResponse.getData());
    }
}
