package com.kodilla.books.domain.response.airTable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import java.util.List;

@Getter
public class RecordsAirTableResponse {

    private List<Record> records;
    @Getter
    public static class Record {
        private String id;
        private String createdTime;
        private Fields fields;

        @Override
        public String toString() {
            return  id;
        }
    }
        @Getter
    public static class Fields {
        @JsonProperty("Jakość")
        private String jakosc;

        @JsonProperty("Pomiar AQIUS")
        private int pomiarAQIUS;

        @JsonProperty("Miasto")
        private String miasto;

        @JsonProperty("Start date")
        private String startDate;

        @JsonProperty("Temperatura")
        private int temperatura;
    }

}
