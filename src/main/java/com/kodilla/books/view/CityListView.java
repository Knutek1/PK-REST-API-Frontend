package com.kodilla.books.view;

import com.kodilla.books.domain.CitiesAirVisualResponse;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Route("cities")
@Component
public class CityListView extends VerticalLayout{

        private final RestTemplate restTemplate;

        @Autowired
        public CityListView(RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
        }

        @PostConstruct
        private void getCities() {
            try {
                String endpointUrl = "http://localhost:8080/v1/Poland/cities";
                CitiesAirVisualResponse citiesAirVisualResponse = restTemplate.getForObject(endpointUrl, CitiesAirVisualResponse.class);
                List<CitiesAirVisualResponse.City> cities = Arrays.asList(citiesAirVisualResponse.getData());

                Grid<CitiesAirVisualResponse.City> grid = new Grid<>(CitiesAirVisualResponse.City.class);
                grid.setItems(cities);
                grid.setColumns("city");

                add(grid);
            } catch (HttpClientErrorException e) {
                System.err.println("Error while fetching data from the server: " + e.getMessage());
            }
        }
    }


