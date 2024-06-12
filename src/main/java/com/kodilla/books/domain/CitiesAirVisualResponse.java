package com.kodilla.books.domain;

import lombok.Getter;

@Getter
public class CitiesAirVisualResponse {
    @Getter
    public static class City {
        private String city;
    }
    private  City[] data;
}
