package com.kodilla.books.view.airVisual;

import com.kodilla.books.domain.response.airVisual.CitiesAirVisualResponse;
import com.kodilla.books.domain.response.airVisual.StatesAirVisualResponse;
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
    private final Grid<CitiesAirVisualResponse.City> grid;

    @Autowired
    public CitiesListView(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

        ComboBox<StatesAirVisualResponse.State> stateComboBox = new ComboBox<>("Select State");
        stateComboBox.setItems(getStates());

        grid = new Grid<>(CitiesAirVisualResponse.City.class);
        grid.setColumns("city");
        grid.setVisible(false);

        Button button = new Button("State Request", event -> {
            StatesAirVisualResponse.State selectedState = stateComboBox.getValue();
            if (selectedState != null) {
                String apiUrl = "http://localhost:8080/v1/Poland/cities";

                CitiesAirVisualResponse citiesAirVisualResponse = restTemplate.getForObject(apiUrl + "?state=" + selectedState.getState(), CitiesAirVisualResponse.class);
                assert citiesAirVisualResponse != null;
                List<CitiesAirVisualResponse.City> cities = Arrays.asList(citiesAirVisualResponse.getData());

                grid.setItems(cities);
                grid.setVisible(true);
            }
        });
        add(stateComboBox, button, grid);
    }

    public List<StatesAirVisualResponse.State> getStates() {
        String endpointUrl = "http://localhost:8080/v1/Poland/states";
        StatesAirVisualResponse statesAirVisualResponse = restTemplate.getForObject(endpointUrl, StatesAirVisualResponse.class);
        assert statesAirVisualResponse != null;
        return Arrays.asList(statesAirVisualResponse.getData());
    }
}
