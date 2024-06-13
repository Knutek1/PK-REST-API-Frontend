package com.kodilla.books.view;

import com.kodilla.books.domain.CityDataAirVisualResponse;
import com.kodilla.books.service.CityDataDisplayer;
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

    @Autowired
    public NearestCityView(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    private void init() {

            VerticalLayout cityDataContainer = new VerticalLayout();
            String endpointUrl = "http://localhost:8080/v1/Poland/nearest_city";
            CityDataAirVisualResponse cityDataAirVisualResponse = restTemplate.getForObject(endpointUrl, CityDataAirVisualResponse.class);

            assert cityDataAirVisualResponse != null;
            CityDataDisplayer.displayCityData(cityDataContainer,cityDataAirVisualResponse);
            add(cityDataContainer);
    }
}


