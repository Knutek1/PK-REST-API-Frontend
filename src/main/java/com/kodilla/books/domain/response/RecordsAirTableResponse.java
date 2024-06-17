package com.kodilla.books.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record RecordsAirTableResponse(List<Record> records) {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Record(String id, String createdTime, Fields fields) {
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Fields(
            @JsonProperty("Jakość") String jakosc,
            @JsonProperty("Pomiar AQIUS") int pomiarAQIUS,
            @JsonProperty("Miasto") String miasto,
            @JsonProperty("Start date") String startDate,
            @JsonProperty("Temperatura") int temperatura) {
    }
}
