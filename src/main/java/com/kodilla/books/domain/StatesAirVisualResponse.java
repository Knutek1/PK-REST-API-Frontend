package com.kodilla.books.domain;

import lombok.Getter;
import lombok.ToString;

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



