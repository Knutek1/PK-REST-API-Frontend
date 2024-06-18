package com.kodilla.books.domain.response.airVisual;

import lombok.Getter;

@Getter
public class StatesAirVisualResponse {
    @Getter

    public static class State {
        private String state;
        @Override
        public String toString() {
            return state ;
        }
    }

    private State[] data;
}



