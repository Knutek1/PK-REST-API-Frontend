package com.kodilla.books.domain.response.airTable;

import lombok.Getter;

@Getter
public class TablesAirTableResponse {
    @Getter
    public static class Table{
        private String id;
        private String name;

        @Override
        public String toString() {
            return name;
        }
    }
    private Table[] tables;
}
