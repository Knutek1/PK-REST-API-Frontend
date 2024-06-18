package com.kodilla.books.domain.response.airTable;

import lombok.Getter;

@Getter
public class BasesAirTableResponse{
    @Getter
    public static class Base{
        private String id;
        private String name;

        @Override
        public String toString() {
            return name;
        }
    }
    private Base[] bases;
}