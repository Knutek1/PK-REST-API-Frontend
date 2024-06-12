package com.kodilla.books.domain;

import lombok.Getter;

@Getter
public class CitiesAirVisualResponse {
    @Getter
    public static class City {
        private String city;

        @Override
        public String toString() {
            return city;
        }
    }
    private  City[] data;
}
