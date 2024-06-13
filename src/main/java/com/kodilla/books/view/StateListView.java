package com.kodilla.books.view;

import com.kodilla.books.domain.StatesAirVisualResponse;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Route("states")
@UIScope
@Component
public class StateListView extends VerticalLayout {

    private final RestTemplate restTemplate;

    @Autowired
    public StateListView(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    private void getStates() {

        String endpointUrl = "http://localhost:8080/v1/Poland/states";
        StatesAirVisualResponse statesAirVisualResponse = restTemplate.getForObject(endpointUrl, StatesAirVisualResponse.class);
        assert statesAirVisualResponse != null;
        List<StatesAirVisualResponse.State> states = Arrays.asList(statesAirVisualResponse.getData());

        Grid<StatesAirVisualResponse.State> grid = new Grid<>(StatesAirVisualResponse.State.class);
        grid.setItems(states);
        grid.setColumns("state");

        add(grid);

    }
}
